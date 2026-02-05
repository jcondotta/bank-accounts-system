package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;

import java.time.Clock;
import java.time.LocalDate;

public record DateOfBirth(LocalDate value) {

  public static final String DATE_OF_BIRTH_NOT_PROVIDED = "Date of birth must be provided.";
  public static final String DATE_OF_BIRTH_NOT_IN_PAST = "Date of birth must be in the past.";

  public DateOfBirth {
    if (value == null) {
      throw new DomainValidationException(DATE_OF_BIRTH_NOT_PROVIDED);
    }

    if (value.isAfter(LocalDate.now(Clock.systemDefaultZone()))) {
      throw new DomainValidationException(DATE_OF_BIRTH_NOT_IN_PAST);
    }
  }

  public static DateOfBirth of(LocalDate value) {
    return new DateOfBirth(value);
  }
}
