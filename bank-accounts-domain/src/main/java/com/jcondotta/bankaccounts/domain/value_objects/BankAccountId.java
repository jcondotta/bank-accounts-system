package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.domain.model.EntityId;
import com.jcondotta.domain.validation.DomainPreconditions;

import java.util.UUID;

public record BankAccountId(UUID value) implements EntityId<UUID> {

  public static final String BANK_ACCOUNT_ID_NOT_PROVIDED = "Bank account id must be provided.";

  public BankAccountId {
    DomainPreconditions.required(value, BANK_ACCOUNT_ID_NOT_PROVIDED);
  }

  public static BankAccountId newId() {
    return new BankAccountId(UUID.randomUUID());
  }

  public static BankAccountId of(UUID value) {
    return new BankAccountId(value);
  }
}