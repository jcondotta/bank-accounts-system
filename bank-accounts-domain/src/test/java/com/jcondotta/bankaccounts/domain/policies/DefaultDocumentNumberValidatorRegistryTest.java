package com.jcondotta.bankaccounts.domain.policies;

import com.jcondotta.bankaccounts.domain.enums.DocumentCountry;
import com.jcondotta.bankaccounts.domain.enums.DocumentType;
import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DefaultDocumentNumberValidatorRegistryTest {

  @Test
  void shouldResolveValidator_whenValidatorExists() {
    var passportValidator = new SpanishPassportNumberValidator();
    var registry = new DefaultDocumentNumberValidatorRegistry(
      List.of(passportValidator)
    );

    var resolved = registry.resolve(
      DocumentCountry.SPAIN,
      DocumentType.PASSPORT
    );

    assertThat(resolved).isSameAs(passportValidator);
  }

  @Test
  void shouldResolveDifferentValidators_whenMultipleAreRegistered() {
    var passport = new SpanishPassportNumberValidator();
    var dni = new SpanishDniNumberValidator();
    var nie = new SpanishNieNumberValidator();

    var registry = new DefaultDocumentNumberValidatorRegistry(
      List.of(passport, dni, nie)
    );

    assertThat(registry.resolve(DocumentCountry.SPAIN, DocumentType.PASSPORT)).isSameAs(passport);
    assertThat(registry.resolve(DocumentCountry.SPAIN, DocumentType.NATIONAL_ID)).isSameAs(dni);
    assertThat(registry.resolve(DocumentCountry.SPAIN, DocumentType.FOREIGNER_ID)).isSameAs(nie);
  }

  @Test
  void shouldThrowDomainValidationException_whenValidatorNotFound() {
    var registry = new DefaultDocumentNumberValidatorRegistry(List.of());

    assertThatThrownBy(() -> registry.resolve(DocumentCountry.SPAIN, DocumentType.PASSPORT))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage("No document validator found for SPAIN - PASSPORT");
  }

  @Test
  void shouldThrowException_whenValidatorsListIsNull() {
    assertThatThrownBy(() -> new DefaultDocumentNumberValidatorRegistry(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("validators must be provided");
  }

  @Test
  void shouldThrowException_whenCountryIsNull() {
    var registry = new DefaultDocumentNumberValidatorRegistry(List.of());

    assertThatThrownBy(() -> registry.resolve(null, DocumentType.PASSPORT))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("country must be provided");
  }

  @Test
  void shouldThrowException_whenTypeIsNull() {
    var registry = new DefaultDocumentNumberValidatorRegistry(List.of());

    assertThatThrownBy(() -> registry.resolve(DocumentCountry.SPAIN, null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("type must be provided");
  }
}