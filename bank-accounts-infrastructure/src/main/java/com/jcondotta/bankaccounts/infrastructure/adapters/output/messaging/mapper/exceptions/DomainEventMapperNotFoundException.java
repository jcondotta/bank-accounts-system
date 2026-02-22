package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.exceptions;

public class DomainEventMapperNotFoundException extends RuntimeException {

  public DomainEventMapperNotFoundException(Class<?> eventType) {
    super("No mapper registered for event type: " + eventType.getName());
  }
}