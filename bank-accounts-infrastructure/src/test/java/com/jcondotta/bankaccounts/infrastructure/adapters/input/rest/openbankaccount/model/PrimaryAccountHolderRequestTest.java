package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model;

import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;
import com.jcondotta.bankaccounts.infrastructure.arguments_provider.BlankValuesArgumentProvider;
import com.jcondotta.bankaccounts.infrastructure.config.ValidatorTestFactory;
import com.jcondotta.bankaccounts.infrastructure.fixtures.AccountHolderFixtures;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PrimaryAccountHolderRequestTest {

  private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

  private static final String VALID_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName().value();
  private static final String VALID_PASSPORT = AccountHolderFixtures.JEFFERSON.getPassportNumber().value();
  private static final LocalDate VALID_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth().value();

  @Test
  void shouldNotDetectConstraintViolation_whenRequestIsValid() {
    var request = new PrimaryAccountHolderRequest(
      VALID_NAME,
      VALID_DATE_OF_BIRTH,
      VALID_PASSPORT
    );

    assertThat(VALIDATOR.validate(request)).isEmpty();
  }

  @ParameterizedTest
  @ArgumentsSource(BlankValuesArgumentProvider.class)
  void shouldDetectConstraintViolation_whenAccountHolderNameIsBlank(String blankName) {
    var request = new PrimaryAccountHolderRequest(
      blankName,
      VALID_DATE_OF_BIRTH,
      VALID_PASSPORT
    );

    assertThat(VALIDATOR.validate(request))
      .hasSize(1)
      .first()
      .satisfies(violation ->
        assertThat(violation.getPropertyPath()).hasToString("accountHolderName")
      );
  }

  @Test
  void shouldDetectConstraintViolation_whenAccountHolderNameExceedsMaxLength() {
    var longName = "A".repeat(AccountHolderName.MAX_LENGTH + 1);

    var request = new PrimaryAccountHolderRequest(
      longName,
      VALID_DATE_OF_BIRTH,
      VALID_PASSPORT
    );

    assertThat(VALIDATOR.validate(request))
      .hasSize(1)
      .first()
      .satisfies(violation ->
        assertThat(violation.getPropertyPath()).hasToString("accountHolderName")
      );
  }

  @Test
  void shouldDetectConstraintViolation_whenDateOfBirthIsNull() {
    var request = new PrimaryAccountHolderRequest(
      VALID_NAME,
      null,
      VALID_PASSPORT
    );

    assertThat(VALIDATOR.validate(request))
      .hasSize(1)
      .first()
      .satisfies(violation ->
        assertThat(violation.getPropertyPath()).hasToString("dateOfBirth")
      );
  }

  @Test
  void shouldDetectConstraintViolation_whenDateOfBirthIsInTheFuture() {
    var futureDate = LocalDate.now().plusDays(1);

    var request = new PrimaryAccountHolderRequest(
      VALID_NAME,
      futureDate,
      VALID_PASSPORT
    );

    assertThat(VALIDATOR.validate(request))
      .hasSize(1)
      .first()
      .satisfies(violation ->
        assertThat(violation.getPropertyPath()).hasToString("dateOfBirth")
      );
  }

  @Test
  void shouldDetectConstraintViolation_whenPassportNumberIsNull() {
    var request = new PrimaryAccountHolderRequest(
      VALID_NAME,
      VALID_DATE_OF_BIRTH,
      null
    );

    assertThat(VALIDATOR.validate(request))
      .hasSize(1)
      .first()
      .satisfies(violation ->
        assertThat(violation.getPropertyPath()).hasToString("passportNumber")
      );
  }

  @Test
  void shouldDetectConstraintViolation_whenPassportNumberLengthIsInvalid() {
    var invalidPassport = "A".repeat(PassportNumber.LENGTH - 1);

    var request = new PrimaryAccountHolderRequest(
      VALID_NAME,
      VALID_DATE_OF_BIRTH,
      invalidPassport
    );

    assertThat(VALIDATOR.validate(request))
      .hasSize(1)
      .first()
      .satisfies(violation ->
        assertThat(violation.getPropertyPath()).hasToString("passportNumber")
      );
  }
}
