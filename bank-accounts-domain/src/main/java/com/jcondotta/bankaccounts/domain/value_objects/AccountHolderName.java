package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import org.apache.commons.lang3.StringUtils;

public record AccountHolderName(String value) {

  public static final int MAX_LENGTH = 255;

  public static final String ACCOUNT_HOLDER_NAME_NOT_NULL = "Account holder name must be provided";
  public static final String ACCOUNT_HOLDER_NAME_NOT_BLANK = "Account holder name must not be empty";
  public static final String ACCOUNT_HOLDER_NAME_TOO_LONG = "Account holder name must not exceed the maximum allowed length";

  public AccountHolderName {
    if (value == null) {
      throw new DomainValidationException(ACCOUNT_HOLDER_NAME_NOT_NULL);
    }

    var normalized = StringUtils.normalizeSpace(value).trim();

    if (normalized.isEmpty()) {
      throw new DomainValidationException(ACCOUNT_HOLDER_NAME_NOT_BLANK);
    }

    if (normalized.length() > MAX_LENGTH) {
      throw new DomainValidationException(ACCOUNT_HOLDER_NAME_TOO_LONG);
    }

    value = normalized;
  }

  public static AccountHolderName of(String value) {
    return new AccountHolderName(value);
  }
}