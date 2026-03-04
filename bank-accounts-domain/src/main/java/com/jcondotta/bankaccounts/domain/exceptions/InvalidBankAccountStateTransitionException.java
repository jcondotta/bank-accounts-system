package com.jcondotta.bankaccounts.domain.exceptions;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.domain.exception.DomainRuleValidationException;

public final class InvalidBankAccountStateTransitionException extends DomainRuleValidationException {

  public InvalidBankAccountStateTransitionException(AccountStatus from,AccountStatus to) {
    super("Cannot transition bank account from " + from + " to " + to);
  }
}
