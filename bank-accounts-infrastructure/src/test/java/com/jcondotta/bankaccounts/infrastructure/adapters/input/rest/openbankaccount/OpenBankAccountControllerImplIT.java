package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.types.DomainEventType;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.OpenBankAccountRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.PrimaryAccountHolderRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.BankAccountOpenedMessage;
import com.jcondotta.bankaccounts.infrastructure.arguments_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.infrastructure.container.KafkaTestContainer;
import com.jcondotta.bankaccounts.infrastructure.container.LocalStackTestContainer;
import com.jcondotta.bankaccounts.infrastructure.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountsURIProperties;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Ignore;
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

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

//@ActiveProfiles("test")
//@ContextConfiguration(initializers = {LocalStackTestContainer.class, KafkaTestContainer.class})
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenBankAccountControllerImplIT {

//  private static final String VALID_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName().value();
//  private static final String VALID_PASSPORT = AccountHolderFixtures.JEFFERSON.getPassportNumber().value();
//  private static final LocalDate VALID_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth().value();
//
//  @Autowired
//  Clock testClockUTC;
//
//  @Autowired
//  ObjectMapper objectMapper;
//
//  @Autowired
//  BankAccountsURIProperties uriProperties;
//
//  @Autowired
//  private TestKafkaListener testKafkaListener;
//
//  RequestSpecification requestSpecification;
//
//  @BeforeAll
//  static void beforeAll() {
//    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//  }
//
//  @BeforeEach
//  void beforeEach(@LocalServerPort int port) {
//    RestAssured.baseURI = "http://localhost";
//    RestAssured.port = port;
//
//    requestSpecification = new RequestSpecBuilder()
//      .setBaseUri(RestAssured.baseURI)
//      .setPort(RestAssured.port)
//      .setBasePath(uriProperties.rootPath())
//      .setContentType(ContentType.JSON)
//      .setAccept(ContentType.JSON)
//      .build();
//  }
//
//  @ParameterizedTest
//  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
//  void shouldReturn201CreatedWithValidLocationHeader_whenRequestIsValid(AccountType accountType, Currency currency) throws JsonProcessingException {
//    var primaryAccountHolderRequest = new PrimaryAccountHolderRequest(VALID_NAME, VALID_PASSPORT, VALID_DATE_OF_BIRTH);
//    var request = new OpenBankAccountRequest(accountType, currency, primaryAccountHolderRequest);
//
//    var response = given()
//      .spec(requestSpecification)
//      .body(objectMapper.writeValueAsString(request))
//    .when()
//      .post()
//    .then()
//      .statusCode(HttpStatus.CREATED.value())
//      .extract()
//      .response();
//
//    var location = response.header("location");
//    assertThat(location).isNotBlank();
//
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
//  }
}