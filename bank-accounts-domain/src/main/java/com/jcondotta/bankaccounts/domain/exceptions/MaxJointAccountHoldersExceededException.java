package com.jcondotta.bankaccounts.domain.exceptions;

public class MaxJointAccountHoldersExceededException extends DomainRuleValidationException {

  public MaxJointAccountHoldersExceededException(int maxAllowed) {
    super("Maximum number of joint account holders exceeded. Max allowed: " + maxAllowed);
  }
}