package com.jcondotta.bankaccounts.domain.enums;

public enum AccountStatus {
  PENDING, ACTIVE, CANCELLED, BLOCKED;

  public boolean isPending() {
    return this == PENDING;
  }

  public boolean isActive() {
    return this == ACTIVE;
  }

  public boolean isCancelled() {
    return this == CANCELLED;
  }

  public boolean isBlocked() {
    return this == BLOCKED;
  }
}