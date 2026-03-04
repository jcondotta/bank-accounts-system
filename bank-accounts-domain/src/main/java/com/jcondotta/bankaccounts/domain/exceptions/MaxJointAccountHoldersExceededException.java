package com.jcondotta.bankaccounts.domain.exceptions;

import com.jcondotta.domain.exception.DomainRuleValidationException;

public class MaxJointAccountHoldersExceededException extends DomainRuleValidationException {

  public MaxJointAccountHoldersExceededException(int maxAllowed) {
    super("Maximum number of joint account holders exceeded. Max allowed: " + maxAllowed);
  }
}