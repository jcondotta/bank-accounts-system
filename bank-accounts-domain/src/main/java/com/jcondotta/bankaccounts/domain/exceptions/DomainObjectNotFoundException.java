package com.jcondotta.bankaccounts.domain.exceptions;

public abstract class DomainObjectNotFoundException extends RuntimeException {

  protected DomainObjectNotFoundException(String message) {
    super(message);
  }
}
