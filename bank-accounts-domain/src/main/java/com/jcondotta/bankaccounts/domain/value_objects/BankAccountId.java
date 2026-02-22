package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;

import java.util.UUID;

public record BankAccountId(UUID value) implements AggregateId{

  public static final String BANK_ACCOUNT_ID_NOT_PROVIDED = "bank account id must be provided.";

  public BankAccountId {
    if (value == null) {
      throw new DomainValidationException(BANK_ACCOUNT_ID_NOT_PROVIDED);
    }
  }

  public static BankAccountId newId() {
    return new BankAccountId(UUID.randomUUID());
  }

  public static BankAccountId of(UUID value) {
    return new BankAccountId(value);
  }
}