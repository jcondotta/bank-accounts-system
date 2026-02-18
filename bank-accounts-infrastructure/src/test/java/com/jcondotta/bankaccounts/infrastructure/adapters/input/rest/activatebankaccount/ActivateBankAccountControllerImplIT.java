package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.activatebankaccount;

import com.jcondotta.bankaccounts.application.usecase.lookup.BankAccountLookupUseCase;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.application.usecase.open.OpenBankAccountUseCase;
import com.jcondotta.bankaccounts.application.usecase.open.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.infrastructure.arguments_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.infrastructure.container.KafkaTestContainer;
import com.jcondotta.bankaccounts.infrastructure.container.LocalStackTestContainer;
import com.jcondotta.bankaccounts.infrastructure.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountsURIProperties;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
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

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ContextConfiguration(initializers = { LocalStackTestContainer.class, KafkaTestContainer.class })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActivateBankAccountControllerImplIT {

  @Autowired
  OpenBankAccountUseCase openBankAccountUseCase;

  @Autowired
  BankAccountLookupUseCase bankAccountLookupUseCase;

  @Autowired
  BankAccountsURIProperties uriProperties;

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
      .setBasePath(uriProperties.bankAccountIdPath() + "/activate")
      .setContentType(ContentType.JSON)
      .setAccept(ContentType.JSON)
      .build();
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldReturn200OkAndActivateBankAccount_whenBankAccountIsPending(AccountType accountType, Currency currency) {
    AccountHolderFixtures primary = AccountHolderFixtures.JEFFERSON;

    BankAccountId bankAccountId = createBankAccount(accountType, currency, primary);

    given()
      .spec(requestSpecification)
      .pathParam("bank-account-id", bankAccountId.value())
    .when()
      .patch()
    .then()
      .statusCode(HttpStatus.NO_CONTENT.value());

    BankAccountDetails details = bankAccountLookupUseCase.lookup(bankAccountId);

    assertThat(details.accountStatus().isActive()).isTrue();
  }

  @Test
  void shouldReturn404NotFound_whenBankAccountDoesNotExist() {
    UUID nonExistentId = UUID.fromString("08d8cf86-bc25-4535-8b88-920c07d3e5fe");

    var problem =
      given()
        .spec(requestSpecification)
        .pathParam("bank-account-id", nonExistentId)
      .when()
        .patch()
      .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .extract()
        .as(ProblemDetail.class);

    assertThat(problem.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());

    assertThat(problem.getInstance()).isEqualTo(uriProperties.activateBankAccountURI(nonExistentId));
  }

  private BankAccountId createBankAccount(
    AccountType accountType,
    Currency currency,
    AccountHolderFixtures fixture
  ) {
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