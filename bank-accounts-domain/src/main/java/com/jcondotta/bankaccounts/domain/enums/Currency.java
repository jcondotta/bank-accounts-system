package com.jcondotta.bankaccounts.domain.enums;

public enum Currency {
  EUR("Euro"),
  USD("US Dollar");

  private final String description;

  Currency(String description) {
    this.description = description;
  }

  public String description() {
    return description;
  }

  public boolean isEuro() {
    return this == EUR;
  }

  public boolean isUSDollar() {
    return this == USD;
  }
}
