package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.arguments_provider.BlankValuesArgumentProvider;
import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PassportNumberTest {

  private static final String VALID_PASSPORT_1 = "A1234567";
  private static final String VALID_PASSPORT_2 = "B7654321";

  @Test
  void shouldCreatePassportNumber_whenValueIsValid() {
    var passportNumber = PassportNumber.of(VALID_PASSPORT_1);

    assertThat(passportNumber)
      .extracting(PassportNumber::value)
      .isEqualTo(VALID_PASSPORT_1);
  }

  @Test
  void shouldNormalizePassportNumber() {
    var passportNumber = PassportNumber.of("  a1234567  ");

    assertThat(passportNumber.value()).isEqualTo(VALID_PASSPORT_1);
  }

  @Test
  void shouldThrowDomainValidationException_whenValueIsNull() {
    assertThatThrownBy(() -> PassportNumber.of(null))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(PassportNumber.PASSPORT_NUMBER_NOT_PROVIDED);
  }

  @ParameterizedTest
  @ArgumentsSource(BlankValuesArgumentProvider.class)
  void shouldThrowDomainValidationException_whenValueIsBlank(String blankValue) {
    assertThatThrownBy(() -> PassportNumber.of(blankValue))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(PassportNumber.PASSPORT_NUMBER_NOT_PROVIDED);
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "A123456",        // too short
    "A12345678",      // too long
    "1234567",        // too short
    "123456789"       // too long
  })
  void shouldThrowDomainValidationException_whenLengthIsInvalid(String invalidLength) {
    assertThatThrownBy(() -> PassportNumber.of(invalidLength))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(PassportNumber.PASSPORT_NUMBER_INVALID_FORMAT);
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "A12345@7",
    "A12-4567",
    "A12 4567",
    "A12345#7"
  })
  void shouldThrowDomainValidationException_whenFormatIsInvalid(String invalidFormat) {
    assertThatThrownBy(() -> PassportNumber.of(invalidFormat))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(PassportNumber.PASSPORT_NUMBER_INVALID_FORMAT);
  }

  @Test
  void shouldBeEqual_whenPassportNumbersHaveSameValue() {
    var passport1 = PassportNumber.of(VALID_PASSPORT_1);
    var passport2 = PassportNumber.of(VALID_PASSPORT_1);

    assertThat(passport1)
      .isEqualTo(passport2)
      .hasSameHashCodeAs(passport2);
  }

  @Test
  void shouldNotBeEqual_whenPassportNumbersHaveDifferentValues() {
    var passport1 = PassportNumber.of(VALID_PASSPORT_1);
    var passport2 = PassportNumber.of(VALID_PASSPORT_2);

    assertThat(passport1).isNotEqualTo(passport2);
  }
}
