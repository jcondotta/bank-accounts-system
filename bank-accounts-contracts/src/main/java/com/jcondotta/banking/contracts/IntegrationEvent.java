package com.jcondotta.banking.contracts;

public interface IntegrationEvent<T> {

  IntegrationEventMetadata metadata();
  T payload();

}