package com.jcondotta.domain.exception;

public abstract class DomainNotFoundException extends DomainException {

  protected DomainNotFoundException(String message) {
    super(message);
  }
}
