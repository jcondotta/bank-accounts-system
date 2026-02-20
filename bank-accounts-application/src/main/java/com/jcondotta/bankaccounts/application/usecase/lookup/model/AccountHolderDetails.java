package com.jcondotta.bankaccounts.application.usecase.lookup.model;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.value_objects.*;

import java.time.Instant;
import java.util.Objects;

public record AccountHolderDetails(
  AccountHolderId id,
  AccountHolderName name,
  PassportNumber passportNumber,
  DateOfBirth dateOfBirth,
  Email email,
  AccountHolderType accountHolderType,
  Instant createdAt
) {
  public AccountHolderDetails {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(passportNumber, "passportNumber");
    Objects.requireNonNull(dateOfBirth, "dateOfBirth");
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(accountHolderType, "accountHolderType");
    Objects.requireNonNull(createdAt, "createdAt");
  }
}
