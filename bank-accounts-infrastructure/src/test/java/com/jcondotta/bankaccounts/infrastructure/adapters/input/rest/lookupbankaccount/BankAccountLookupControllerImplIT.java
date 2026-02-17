package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.bankaccounts.application.usecase.activate.ActivateBankAccountUseCase;
import com.jcondotta.bankaccounts.application.usecase.activate.model.ActivateBankAccountCommand;
import com.jcondotta.bankaccounts.application.usecase.addholder.AddJointAccountHolderUseCase;
import com.jcondotta.bankaccounts.application.usecase.addholder.model.AddJointAccountHolderCommand;
import com.jcondotta.bankaccounts.application.usecase.open.OpenBankAccountUseCase;
import com.jcondotta.bankaccounts.application.usecase.open.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.application.usecase.open.model.OpenBankAccountResult;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.mapper.AddJointAccountHolderRequestControllerMapper;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.model.BankAccountLookupResponse;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.mapper.OpenBankAccountRequestControllerMapper;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.OpenBankAccountRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.PrimaryAccountHolderRequest;
import com.jcondotta.bankaccounts.infrastructure.arguments_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.infrastructure.container.KafkaTestContainer;
import com.jcondotta.bankaccounts.infrastructure.container.LocalStackTestContainer;
import com.jcondotta.bankaccounts.infrastructure.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountsURIProperties;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ContextConfiguration(initializers = {LocalStackTestContainer.class, KafkaTestContainer.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankAccountLookupControllerImplIT {

  @Autowired
  Clock fixedClock;

  @Autowired
  BankAccountsURIProperties uriProperties;

  @Autowired
  OpenBankAccountUseCase openBankAccountUseCase;

  @Autowired
  ActivateBankAccountUseCase activateBankAccountUseCase;

  @Autowired
  AddJointAccountHolderUseCase addJointAccountHolderUseCase;

  RequestSpecification requestSpecification;

  @BeforeAll
  static void beforeAll() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @BeforeEach
  void beforeEach(@LocalServerPort int port) {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;

    requestSpecification = new RequestSpecBuilder()
      .setBaseUri(RestAssured.baseURI)
      .setPort(RestAssured.port)
      .setBasePath(uriProperties.bankAccountIdPath())
      .setContentType(ContentType.JSON)
      .setAccept(ContentType.JSON)
      .build();
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldReturn200OkWithBankAccountDetails_whenBankAccountIsFound(AccountType accountType, Currency currency) {
    AccountHolderFixtures primaryHolderFixture = AccountHolderFixtures.JEFFERSON;
    BankAccountId bankAccountId = createBankAccount(accountType, currency, primaryHolderFixture);

    var bankAccountLookupResponse = given()
      .spec(requestSpecification)
      .pathParam("bank-account-id", bankAccountId.value())
    .when()
      .get()
    .then()
      .statusCode(HttpStatus.OK.value())
      .extract()
      .response()
      .as(BankAccountLookupResponse.class);

    assertThat(bankAccountLookupResponse.bankAccount())
      .satisfies(bankAccount -> Assertions.assertAll(
        () -> assertThat(bankAccount.bankAccountId()).isEqualTo(bankAccountId.value()),
        () -> assertThat(bankAccount.accountType()).isEqualTo(accountType),
        () -> assertThat(bankAccount.currency()).isEqualTo(currency),
        () -> assertThat(bankAccount.iban()).isNotBlank(),
        () -> assertThat(bankAccount.openingDate()).isEqualTo(ZonedDateTime.now(fixedClock)),
        () -> assertThat(bankAccount.accountHolders())
          .hasSize(1)
          .singleElement()
          .satisfies(accountHolderDetails -> {
            assertThat(accountHolderDetails.accountHolderName()).isEqualTo(primaryHolderFixture.getAccountHolderName().value());
            assertThat(accountHolderDetails.passportNumber()).isEqualTo(primaryHolderFixture.getPassportNumber().value());
            assertThat(accountHolderDetails.dateOfBirth()).isEqualTo(primaryHolderFixture.getDateOfBirth().value());
            assertThat(accountHolderDetails.email()).isEqualTo(primaryHolderFixture.getEmail().value());
            assertThat(accountHolderDetails.accountHolderType().isPrimary()).isTrue();
            assertThat(accountHolderDetails.createdAt()).isEqualTo(ZonedDateTime.now(fixedClock));
          })
      ));
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldReturn200OkWithPrimaryAndJointAccountHolders_whenBankAccountHasMultipleHolders(AccountType accountType, Currency currency) {
    AccountHolderFixtures primaryHolderFixture = AccountHolderFixtures.JEFFERSON;
    BankAccountId bankAccountId = createBankAccount(accountType, currency, primaryHolderFixture);
    activateBankAccountUseCase.execute(new ActivateBankAccountCommand(bankAccountId));

    AccountHolderFixtures jointHolderFixture = AccountHolderFixtures.PATRIZIO;

    addJointAccountHolderUseCase.execute(
      new AddJointAccountHolderCommand(
        bankAccountId,
        jointHolderFixture.getAccountHolderName(),
        jointHolderFixture.getPassportNumber(),
        jointHolderFixture.getDateOfBirth(),
        jointHolderFixture.getEmail()
      )
    );

    var bankAccountLookupResponse =
      given()
        .spec(requestSpecification)
        .pathParam("bank-account-id", bankAccountId.value())
      .when()
        .get()
      .then()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .as(BankAccountLookupResponse.class);

    assertThat(bankAccountLookupResponse.bankAccount())
      .satisfies(bankAccount -> Assertions.assertAll(
        () -> assertThat(bankAccount.bankAccountId()).isEqualTo(bankAccountId.value()),
        () -> assertThat(bankAccount.accountType()).isEqualTo(accountType),
        () -> assertThat(bankAccount.currency()).isEqualTo(currency),
        () -> assertThat(bankAccount.iban()).isNotBlank(),
        () -> assertThat(bankAccount.openingDate()).isEqualTo(ZonedDateTime.now(fixedClock)),
        () -> assertThat(bankAccount.accountHolders())
          .hasSize(2)
          .anySatisfy(accountHolderDetails -> {
            assertThat(accountHolderDetails.accountHolderName()).isEqualTo(primaryHolderFixture.getAccountHolderName().value());
            assertThat(accountHolderDetails.passportNumber()).isEqualTo(primaryHolderFixture.getPassportNumber().value());
            assertThat(accountHolderDetails.dateOfBirth()).isEqualTo(primaryHolderFixture.getDateOfBirth().value());
            assertThat(accountHolderDetails.email()).isEqualTo(primaryHolderFixture.getEmail().value());
            assertThat(accountHolderDetails.accountHolderType().isPrimary()).isTrue();
            assertThat(accountHolderDetails.createdAt()).isEqualTo(ZonedDateTime.now(fixedClock));
          })
          .anySatisfy(accountHolderDetails -> {
            assertThat(accountHolderDetails.accountHolderName()).isEqualTo(jointHolderFixture.getAccountHolderName().value());
            assertThat(accountHolderDetails.passportNumber()).isEqualTo(jointHolderFixture.getPassportNumber().value());
            assertThat(accountHolderDetails.dateOfBirth()).isEqualTo(jointHolderFixture.getDateOfBirth().value());
            assertThat(accountHolderDetails.email()).isEqualTo(jointHolderFixture.getEmail().value());
            assertThat(accountHolderDetails.accountHolderType().isJoint()).isTrue();
            assertThat(accountHolderDetails.createdAt()).isEqualTo(ZonedDateTime.now(fixedClock));
          })
      ));
  }

  @Test
  void shouldReturn404NotFound_whenBankAccountIsNotFound() {
    var nonExistentBankAccountId = UUID.fromString("08d8cf86-bc25-4535-8b88-920c07d3e5fe");

    var problemDetail =
      given()
        .spec(requestSpecification)
        .pathParam("bank-account-id", nonExistentBankAccountId)
      .when()
        .get()
      .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .extract()
        .as(ProblemDetail.class);

    assertThat(problemDetail.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    assertThat(problemDetail.getInstance()).isEqualTo(uriProperties.bankAccountURI(nonExistentBankAccountId));
  }

  private BankAccountId createBankAccount(AccountType accountType, Currency currency, AccountHolderFixtures fixture) {
    OpenBankAccountCommand command = new OpenBankAccountCommand(
      fixture.getAccountHolderName(),
      fixture.getPassportNumber(),
      fixture.getDateOfBirth(),
      fixture.getEmail(),
      accountType,
      currency
    );

    return openBankAccountUseCase.execute(command).bankAccountId();
  }
}