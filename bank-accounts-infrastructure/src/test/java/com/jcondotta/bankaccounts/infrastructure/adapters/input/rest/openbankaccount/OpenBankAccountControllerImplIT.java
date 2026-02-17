package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.OpenBankAccountRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.PrimaryAccountHolderRequest;
import com.jcondotta.bankaccounts.infrastructure.arguments_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.infrastructure.arguments_provider.BlankValuesArgumentProvider;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@ActiveProfiles("test")
@ContextConfiguration(initializers = { LocalStackTestContainer.class, KafkaTestContainer.class })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenBankAccountControllerImplIT {

  private static final String VALID_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName().value();
  private static final String VALID_PASSPORT = AccountHolderFixtures.JEFFERSON.getPassportNumber().value();
  private static final LocalDate VALID_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth().value();
  private static final String VALID_EMAIL = AccountHolderFixtures.JEFFERSON.getEmail().value();

  private static final Currency CURRENCY_EUR = Currency.EUR;
  private static final AccountType ACCOUNT_TYPE_CHECKING = AccountType.CHECKING;
//
//  @Autowired
//  Clock testClockUTC;
//
  @Autowired
  ObjectMapper objectMapper;
//
  @Autowired
  BankAccountsURIProperties uriProperties;
//
//  @Autowired
//  private TestKafkaListener testKafkaListener;
//
  RequestSpecification requestSpecification;
//
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
      .setBasePath(uriProperties.rootPath())
      .setContentType(ContentType.JSON)
      .setAccept(ContentType.JSON)
      .build();
  }
//
  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldReturn201CreatedWithValidLocationHeader_whenRequestIsValid(AccountType accountType, Currency currency) throws JsonProcessingException {
    var primaryAccountHolderRequest = new PrimaryAccountHolderRequest(VALID_NAME, VALID_PASSPORT, VALID_DATE_OF_BIRTH, VALID_EMAIL);
    var request = new OpenBankAccountRequest(accountType, currency, primaryAccountHolderRequest);

    var response = given()
      .spec(requestSpecification)
      .body(objectMapper.writeValueAsString(request))
    .when()
      .post()
    .then()
      .statusCode(HttpStatus.CREATED.value())
      .extract()
      .response();

    var location = response.header("location");
    assertThat(location).isNotBlank();

//    UUID bankAccountId = UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
//
//    try {
//      ConsumerRecord<String, EventEnvelope<BankAccountOpenedMessage>> record =
//        testKafkaListener.poll(5, TimeUnit.SECONDS);
//
//      assertThat(record).isNotNull();
//      assertThat(record.key()).hasToString(bankAccountId.toString());
//
//      EventEnvelope<BankAccountOpenedMessage> envelope = record.value();
//      assertThat(envelope.metadata())
//        .satisfies(metadata -> {
//          assertThat(metadata.eventId()).isNotNull();
//          assertThat(metadata.correlationId()).isNotNull();
//          assertThat(metadata.publishedAt()).isNotNull();
//          assertThat(metadata.eventType()).isEqualTo(DomainEventType.BANK_ACCOUNT_OPENED.value());
//        });
//
//      var bankAccountOpenedMessage = objectMapper.convertValue(envelope.payload(), BankAccountOpenedMessage.class);
//
//      assertThat(bankAccountOpenedMessage)
//        .satisfies(message -> {
//          assertThat(message.bankAccountId()).isEqualTo(bankAccountId);
//          assertThat(message.accountType()).isEqualTo(accountType.toString());
//          assertThat(message.currency()).isEqualTo(currency.toString());
//          assertThat(message.primaryAccountHolderId()).isNotNull();
//          assertThat(message.occurredAt()).isEqualTo(ZonedDateTime.now(testClockUTC).toString());
//
//        });
//
//    } catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    }
  }

  @ParameterizedTest
  @ArgumentsSource(BlankValuesArgumentProvider.class)
  void shouldReturn422WithValidationProblemDetails_whenAccountHolderNameIsBlank(String blankName) throws JsonProcessingException {

    var primaryAccountHolderRequest =
      new PrimaryAccountHolderRequest(blankName, VALID_PASSPORT, VALID_DATE_OF_BIRTH, VALID_EMAIL);

    var request =
      new OpenBankAccountRequest(AccountType.CHECKING, Currency.EUR, primaryAccountHolderRequest);

    given()
      .spec(requestSpecification)
      .body(objectMapper.writeValueAsString(request))
    .when()
      .post()
    .then()
      .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
      .body("instance", equalTo(uriProperties.rootPath()))
      .body("properties.errors", hasSize(1))
      .body("properties.errors[0].field", equalTo("accountHolder.accountHolderName"))
      .body("properties.errors[0].messages[0]", equalTo("must not be blank"));
  }
}