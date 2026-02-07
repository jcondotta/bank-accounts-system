package com.jcondotta.bankaccounts.domain.exceptions;

public abstract class DomainRuleValidationException extends RuntimeException {

  protected DomainRuleValidationException(String message) {
    super(message);
  }
}
