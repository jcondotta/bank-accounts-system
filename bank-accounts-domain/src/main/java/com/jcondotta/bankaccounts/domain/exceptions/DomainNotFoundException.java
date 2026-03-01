package com.jcondotta.bankaccounts.domain.exceptions;

public abstract class DomainNotFoundException extends DomainException {

  protected DomainNotFoundException(String message) {
    super(message);
  }
}
