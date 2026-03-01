package com.jcondotta.bankaccounts.domain.value_objects.address;

import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;

import static com.jcondotta.bankaccounts.domain.validation.DomainPreconditions.requiredNotBlank;

public record PostalCode(String value) {

  public static final String MUST_NOT_BE_EMPTY = "Postal code must not be empty";
  public static final String MUST_NOT_EXCEED_LENGTH =
    "Postal code must not exceed %d characters";

  public static final int MAX_LENGTH = 20;

  public PostalCode {
    requiredNotBlank(value, MUST_NOT_BE_EMPTY);

    if (value.length() > MAX_LENGTH) {
      throw new DomainValidationException(
        MUST_NOT_EXCEED_LENGTH.formatted(MAX_LENGTH)
      );
    }
  }

  public static PostalCode of(String value) {
    return new PostalCode(value);
  }
}