package com.jcondotta.domain.exception;

public abstract class DomainRuleValidationException extends DomainException {

  protected DomainRuleValidationException(String message) {
    super(message);
  }
}
