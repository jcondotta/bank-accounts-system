package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;

public record PassportNumber(String value) {

  public static final int LENGTH = 8;
  public static final String PASSPORT_NUMBER_NOT_PROVIDED = "Passport number must be provided.";
  public static final String PASSPORT_NUMBER_INVALID_FORMAT = "Passport number format is invalid.";

  public PassportNumber {
    if (value == null) {
      throw new DomainValidationException(PASSPORT_NUMBER_NOT_PROVIDED);
    }

    var normalized = value.trim().toUpperCase();

    if (normalized.isEmpty()) {
      throw new DomainValidationException(PASSPORT_NUMBER_NOT_PROVIDED);
    }

    if (normalized.length() != LENGTH) {
      throw new DomainValidationException(PASSPORT_NUMBER_INVALID_FORMAT);
    }

    if (!normalized.matches("^[A-Z0-9]+$")) {
      throw new DomainValidationException(PASSPORT_NUMBER_INVALID_FORMAT);
    }

    value = normalized;
  }

  public static PassportNumber of(String value) {
    return new PassportNumber(value);
  }
}