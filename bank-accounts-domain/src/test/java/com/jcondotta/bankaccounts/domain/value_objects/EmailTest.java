package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

  @ParameterizedTest
  @CsvSource({
    "'John.Doe@Example.com', 'john.doe@example.com'",
    "'   John.Doe@Example.com   ', 'john.doe@example.com'",
    "'TEST@DOMAIN.COM', 'test@domain.com'",
    "'user+alias@Sub.Domain.Co.Uk', 'user+alias@sub.domain.co.uk'"
  })
  void shouldNormalizeEmail_whenValidEmailIsProvided(String raw, String expected) {
    Email email = Email.of(raw);
    assertEquals(expected, email.value());
  }

  @Test
  void shouldBeEqual_whenEmailsHaveSameNormalizedValue() {
    Email email1 = Email.of("Test@Example.com");
    Email email2 = Email.of("test@example.com");

    assertEquals(email1, email2);
    assertEquals(email1.hashCode(), email2.hashCode());
  }

  @Test
  void shouldThrowDomainValidationException_whenEmailIsNull() {
    var exception = assertThrows(DomainValidationException.class, () -> Email.of(null));
    assertEquals(Email.EMAIL_NOT_PROVIDED, exception.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "",
    "   "
  })
  void shouldThrowDomainValidationException_whenEmailIsBlank(String raw) {
    var exception = assertThrows(DomainValidationException.class, () -> Email.of(raw));

    assertEquals(Email.EMAIL_NOT_PROVIDED, exception.getMessage());
  }

  @Test
  void shouldThrowDomainValidationException_whenEmailExceedsMaxLength() {
    String localPart = "a".repeat(250);
    String email = localPart + "@t.com";

    var exception = assertThrows(DomainValidationException.class, () -> Email.of(email));
    assertEquals(Email.EMAIL_EXCEEDS_MAX_LENGTH, exception.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "invalid",
    "invalid@",
    "@domain.com",
    "test@domain",
    "test@.com",
    "john doe@email.com",
    "john@do main.com",
    "john @email.com",
    "john@ email.com"
  })
  void shouldThrowDomainValidationException_whenEmailFormatIsInvalid(String raw) {
    var exception = assertThrows(DomainValidationException.class, () -> Email.of(raw));

    assertEquals(Email.EMAIL_INVALID_FORMAT, exception.getMessage());
  }
}