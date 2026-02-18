package com.jcondotta.bankaccounts.domain.exceptions;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;

public final class InvalidBankAccountStateTransitionException extends DomainRuleValidationException {

  public InvalidBankAccountStateTransitionException(AccountStatus from,AccountStatus to) {
    super("Cannot transition bank account from " + from + " to " + to);
  }
}
