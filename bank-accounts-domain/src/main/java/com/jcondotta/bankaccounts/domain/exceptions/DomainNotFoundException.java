package com.jcondotta.bankaccounts.domain.exceptions;

public abstract class DomainNotFoundException extends RuntimeException {

  protected DomainNotFoundException(String message) {
    super(message);
  }
}
