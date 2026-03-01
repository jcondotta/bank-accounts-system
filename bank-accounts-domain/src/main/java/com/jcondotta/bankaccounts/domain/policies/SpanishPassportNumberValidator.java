package com.jcondotta.bankaccounts.domain.policies;

import com.jcondotta.bankaccounts.domain.enums.DocumentCountry;
import com.jcondotta.bankaccounts.domain.enums.DocumentType;
import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import com.jcondotta.bankaccounts.domain.value_objects.personal.DocumentNumber;

public final class SpanishPassportNumberValidator implements DocumentNumberValidator {

  public static final String PASSPORT_NUMBER_INVALID_FORMAT = "Document number is invalid for Spanish passport.";
  private static final String PASSPORT_REGEX = "^[A-Z]{3}[0-9]{6}$";

  @Override
  public DocumentCountry supportedCountry() {
    return DocumentCountry.SPAIN;
  }

  @Override
  public DocumentType supportedType() {
    return DocumentType.PASSPORT;
  }

  @Override
  public void validate(DocumentNumber documentNumber) {
    if (!documentNumber.value().matches(PASSPORT_REGEX)) {
      throw new DomainValidationException(PASSPORT_NUMBER_INVALID_FORMAT);
    }
  }
}