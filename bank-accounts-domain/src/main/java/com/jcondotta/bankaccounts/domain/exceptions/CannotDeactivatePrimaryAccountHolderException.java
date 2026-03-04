package com.jcondotta.bankaccounts.domain.exceptions;

import com.jcondotta.domain.exception.DomainRuleValidationException;

public class CannotDeactivatePrimaryAccountHolderException extends DomainRuleValidationException {

  public static final String PRIMARY_ACCOUNT_HOLDER_CANNOT_BE_DEACTIVATED =
    "Primary account holder cannot be deactivated";

  public CannotDeactivatePrimaryAccountHolderException() {
    super(PRIMARY_ACCOUNT_HOLDER_CANNOT_BE_DEACTIVATED);
  }
}