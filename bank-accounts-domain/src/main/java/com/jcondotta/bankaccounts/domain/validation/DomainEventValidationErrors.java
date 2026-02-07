package com.jcondotta.bankaccounts.domain.validation;

public interface DomainEventValidationErrors extends DomainValidationErrors {

  String EVENT_ID_NOT_NULL = "event id must not be null";
  String EVENT_OCCURRED_AT_NOT_NULL = "event occurred at must not be null";
}