package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.bankaccounts.application.usecase.activate.ActivateBankAccountUseCase;
import com.jcondotta.bankaccounts.application.usecase.activate.model.ActivateBankAccountCommand;
import com.jcondotta.bankaccounts.application.usecase.addholder.AddJointAccountHolderUseCase;
import com.jcondotta.bankaccounts.application.usecase.addholder.model.AddJointAccountHolderCommand;
import com.jcondotta.bankaccounts.application.usecase.lookup.BankAccountLookupUseCase;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.application.usecase.open.OpenBankAccountUseCase;
import com.jcondotta.bankaccounts.application.usecase.open.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.model.AddJointAccountHolderRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.model.BankAccountLookupResponse;
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
import java.time.ZonedDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ContextConfiguration(initializers = {LocalStackTestContainer.class, KafkaTestContainer.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddJointAccountHolderControllerImplIT {

  @Autowired
  Clock fixedClock;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  BankAccountsURIProperties uriProperties;

  @Autowired
  OpenBankAccountUseCase openBankAccountUseCase;

  @Autowired
  ActivateBankAccountUseCase activateBankAccountUseCase;

  @Autowired
  BankAccountLookupUseCase bankAccountLookupUseCase;

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
      .setBasePath(uriProperties.bankAccountIdPath() + "/account-holders")
      .setContentType(ContentType.JSON)
      .setAccept(ContentType.JSON)
      .build();
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldReturn200OkAndAddJointAccountHolder_whenBankAccountIsActive(AccountType accountType, Currency currency) throws JsonProcessingException {
    AccountHolderFixtures primaryHolderFixture = AccountHolderFixtures.JEFFERSON;
    BankAccountId bankAccountId = createBankAccount(accountType, currency, primaryHolderFixture);
    activateBankAccountUseCase.execute(new ActivateBankAccountCommand(bankAccountId));

    AccountHolderFixtures jointHolderFixture = AccountHolderFixtures.PATRIZIO;
    var addJointAccountHolderRequest =
      new AddJointAccountHolderRequest(
        jointHolderFixture.getAccountHolderName().value(),
        jointHolderFixture.getPassportNumber().value(),
        jointHolderFixture.getDateOfBirth().value(),
        jointHolderFixture.getEmail().value()
      );

    given()
      .spec(requestSpecification)
      .pathParam("bank-account-id", bankAccountId.value())
      .body(addJointAccountHolderRequest)
    .when()
      .post()
    .then()
      .statusCode(HttpStatus.OK.value());

    BankAccountDetails bankAccountDetails = bankAccountLookupUseCase.lookup(bankAccountId);

    assertThat(bankAccountDetails.accountHolders())
      .hasSize(2)
      .anySatisfy(holder -> {
        assertThat(holder.accountHolderName()).isEqualTo(jointHolderFixture.getAccountHolderName());
        assertThat(holder.passportNumber()).isEqualTo(jointHolderFixture.getPassportNumber());
        assertThat(holder.dateOfBirth()).isEqualTo(jointHolderFixture.getDateOfBirth());
        assertThat(holder.email()).isEqualTo(jointHolderFixture.getEmail());
        assertThat(holder.accountHolderType().isJoint()).isTrue();
        assertThat(holder.createdAt()).isEqualTo(ZonedDateTime.now(fixedClock));
      });
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldReturn422UnprocessableEntity_whenBankAccountIsNotActive(AccountType accountType, Currency currency) throws JsonProcessingException {
    AccountHolderFixtures primaryHolderFixture = AccountHolderFixtures.JEFFERSON;
    BankAccountId bankAccountId = createBankAccount(accountType, currency, primaryHolderFixture);

    AccountHolderFixtures jointHolderFixture = AccountHolderFixtures.PATRIZIO;

    var request =
      new AddJointAccountHolderRequest(
        jointHolderFixture.getAccountHolderName().value(),
        jointHolderFixture.getPassportNumber().value(),
        jointHolderFixture.getDateOfBirth().value(),
        jointHolderFixture.getEmail().value()
      );

    given()
      .spec(requestSpecification)
      .pathParam("bank-account-id", bankAccountId.value())
      .body(request)
    .when()
      .post()
    .then()
      .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

    BankAccountDetails bankAccountDetails = bankAccountLookupUseCase.lookup(bankAccountId);
    assertThat(bankAccountDetails.accountHolders()).hasSize(1);
  }

  @Test
  void shouldReturn404NotFound_whenBankAccountIsNotFound() throws JsonProcessingException {
    var nonExistentBankAccountId = UUID.fromString("08d8cf86-bc25-4535-8b88-920c07d3e5fe");

    AccountHolderFixtures jointHolderFixture = AccountHolderFixtures.PATRIZIO;
    var addJointAccountHolderRequest =
      new AddJointAccountHolderRequest(
        jointHolderFixture.getAccountHolderName().value(),
        jointHolderFixture.getPassportNumber().value(),
        jointHolderFixture.getDateOfBirth().value(),
        jointHolderFixture.getEmail().value()
      );

    var problemDetail =
      given()
        .spec(requestSpecification)
        .pathParam("bank-account-id", nonExistentBankAccountId)
        .body(addJointAccountHolderRequest)
      .when()
        .post()
      .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .extract()
        .as(ProblemDetail.class);

    assertThat(problemDetail.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    assertThat(problemDetail.getInstance()).isEqualTo(uriProperties.accountHoldersURI(nonExistentBankAccountId));
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