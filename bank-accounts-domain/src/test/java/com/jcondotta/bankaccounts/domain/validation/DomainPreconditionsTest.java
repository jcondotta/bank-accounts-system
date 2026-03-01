package com.jcondotta.bankaccounts.domain.validation;

import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DomainPreconditionsTest {

  @Test
  void shouldReturnValue_whenRequiredValueIsNotNull() {
    String value = "valid";

    String result = DomainPreconditions.required(value, "message");

    assertThat(result).isEqualTo(value);
  }

  @Test
  void shouldThrowException_whenRequiredValueIsNull() {
    assertThatThrownBy(() -> DomainPreconditions.required(null, "error"))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage("error");
  }

  @Test
  void shouldReturnValue_whenRequiredNotBlankValueIsValid() {
    String value = "valid";

    String result = DomainPreconditions.requiredNotBlank(value, "message");

    assertThat(result).isEqualTo(value);
  }

  @Test
  void shouldThrowException_whenRequiredNotBlankValueIsNull() {
    assertThatThrownBy(() -> DomainPreconditions.requiredNotBlank(null, "error"))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage("error");
  }

  @Test
  void shouldThrowException_whenRequiredNotBlankValueIsBlank() {
    assertThatThrownBy(() -> DomainPreconditions.requiredNotBlank("   ", "error"))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage("error");
  }

  @Test
  void shouldNotAllowInstantiation() throws Exception {
    Constructor<DomainPreconditions> constructor =
      DomainPreconditions.class.getDeclaredConstructor();

    constructor.setAccessible(true);

    assertThatThrownBy(constructor::newInstance)
      .hasCauseInstanceOf(UnsupportedOperationException.class)
      .hasRootCauseMessage("No instances allowed for this class");
  }
}