package com.jcondotta.bankaccounts.domain.exceptions;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;

public final class BankAccountNotActiveException extends DomainRuleValidationException {

  public BankAccountNotActiveException(AccountStatus status) {
    super("Bank account must be ACTIVE to perform this operation. Current status: " + status);
  }
}