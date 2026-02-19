package com.jcondotta.bankaccounts.domain.enums;

public enum AccountStatus {
  PENDING, ACTIVE, BLOCKED, CLOSED;

  public boolean isPending() {
    return this == PENDING;
  }
  public boolean isActive() {
    return this == ACTIVE;
  }
  public boolean isBlocked() {
    return this == BLOCKED;
  }
  public boolean isClosed() {
    return this == CLOSED;
  }
}