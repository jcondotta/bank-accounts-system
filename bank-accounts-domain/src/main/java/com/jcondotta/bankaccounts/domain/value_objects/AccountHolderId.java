package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.validation.DomainPreconditions;

import java.util.UUID;

public record AccountHolderId(UUID value) {

  public static final String ACCOUNT_HOLDER_ID_NOT_PROVIDED = "Account holder id must be provided.";

  public AccountHolderId {
    DomainPreconditions.required(value, ACCOUNT_HOLDER_ID_NOT_PROVIDED);
  }

  public static AccountHolderId newId() {
    return new AccountHolderId(UUID.randomUUID());
  }

  public static AccountHolderId of(UUID value) {
    return new AccountHolderId(value);
  }
}
