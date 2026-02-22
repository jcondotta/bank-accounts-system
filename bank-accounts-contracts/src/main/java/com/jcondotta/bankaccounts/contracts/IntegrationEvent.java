package com.jcondotta.bankaccounts.contracts;

public interface IntegrationEvent<T> {

  IntegrationEventMetadata metadata();
  T payload();

}