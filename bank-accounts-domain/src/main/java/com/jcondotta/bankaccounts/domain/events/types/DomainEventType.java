package com.jcondotta.bankaccounts.domain.events.types;

public enum DomainEventType {

  BANK_ACCOUNT_OPENED,
  BANK_ACCOUNT_ACTIVATED,
  BANK_ACCOUNT_BLOCKED,
  BANK_ACCOUNT_UNBLOCKED,
  BANK_ACCOUNT_CLOSED,
  JOINT_ACCOUNT_HOLDER_ADDED;
}