package com.jcondotta.bankaccounts.domain.exceptions;

public abstract class DomainRuleValidationException extends DomainException {

  protected DomainRuleValidationException(String message) {
    super(message);
  }
}
