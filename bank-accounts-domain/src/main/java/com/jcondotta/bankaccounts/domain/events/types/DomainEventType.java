package com.jcondotta.bankaccounts.domain.events.types;

public enum DomainEventType {

  BANK_ACCOUNT_OPENED("bank-account-opened"),
  BANK_ACCOUNT_ACTIVATED("bank-account-activated"),
  BANK_ACCOUNT_BLOCKED("bank-account-blocked"),
  BANK_ACCOUNT_UNBLOCKED("bank-account-unblocked"),
  BANK_ACCOUNT_CLOSED("bank-account-closed"),
  JOINT_ACCOUNT_HOLDER_ADDED("joint-account-holder-added");

  private final String value;

  DomainEventType(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}