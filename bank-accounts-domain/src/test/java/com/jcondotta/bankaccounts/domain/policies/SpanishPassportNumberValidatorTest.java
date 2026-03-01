package com.jcondotta.bankaccounts.domain.policies;

import com.jcondotta.bankaccounts.domain.enums.DocumentCountry;
import com.jcondotta.bankaccounts.domain.enums.DocumentType;
import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import com.jcondotta.bankaccounts.domain.value_objects.personal.DocumentNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class SpanishPassportNumberValidatorTest {

  private final SpanishPassportNumberValidator validator = new SpanishPassportNumberValidator();

  @Test
  void shouldSupportSpainAsCountry() {
    assertThat(validator.supportedCountry()).isEqualTo(DocumentCountry.SPAIN);
  }

  @Test
  void shouldSupportPassportAsType() {
    assertThat(validator.supportedType()).isEqualTo(DocumentType.PASSPORT);
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "ABC123456",
    "XYZ000001",
    "AAA999999",
    "QWE123000",
    "MNB456789"
  })
  void shouldNotThrowException_whenPassportNumberIsValid(String validValue) {
    var validNumber = DocumentNumber.of(validValue);

    assertThatCode(() -> validator.validate(validNumber)).doesNotThrowAnyException();
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "123ABC",
    "abc123456",
    "AB12345",
    "ABCD123456",
    "ABC12345",
    "ABC1234567",
    "A1C123456",
  })
  void shouldThrowException_whenPassportNumberIsInvalid(String invalidValue) {
    var invalidNumber = DocumentNumber.of(invalidValue);

    assertThatThrownBy(() -> validator.validate(invalidNumber))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(SpanishPassportNumberValidator.PASSPORT_NUMBER_INVALID_FORMAT);
  }
}