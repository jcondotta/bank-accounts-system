package com.jcondotta.bankaccounts.domain.exceptions;

public class DomainValidationException extends DomainException {
  public DomainValidationException(String message) {
    super(message);
  }
}