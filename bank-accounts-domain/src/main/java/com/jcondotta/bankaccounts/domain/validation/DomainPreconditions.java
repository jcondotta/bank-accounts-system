package com.jcondotta.bankaccounts.domain.validation;

import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;

public final class DomainPreconditions {

  private DomainPreconditions() {
    throw new UnsupportedOperationException("No instances allowed for this class");
  }

  public static <T> T required(T value, String message) {
    if (value == null) {
      throw new DomainValidationException(message);
    }
    return value;
  }

  public static String requiredNotBlank(String value, String message) {
    if (value == null || value.isBlank()) {
      throw new DomainValidationException(message);
    }
    return value;
  }
}