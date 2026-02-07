package com.jcondotta.bankaccounts.domain.validation;

public interface DomainValidationErrors {

//  private DomainValidationErrors() {}

  public static final class DomainEvent {

    private DomainEvent() {}

    public static final String EVENT_ID_NOT_NULL = "event id must not be null";
    public static final String EVENT_OCCURRED_AT_NOT_NULL = "event occurred at must not be null";
  }

  public static final String CREATED_AT_NOT_NULL = "createdAt must not be null";
  public static final String CREATED_AT_IN_FUTURE = "createdAt must not be in the future";

}
