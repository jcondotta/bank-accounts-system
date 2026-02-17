package com.jcondotta.bankaccounts.application.usecase.lookup.model;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.value_objects.*;

import java.time.ZonedDateTime;
import java.util.Objects;

public record AccountHolderDetails(
  AccountHolderId accountHolderId,
  AccountHolderName accountHolderName,
  PassportNumber passportNumber,
  DateOfBirth dateOfBirth,
  Email email,
  AccountHolderType accountHolderType,
  ZonedDateTime createdAt
) {
  public AccountHolderDetails {
    Objects.requireNonNull(accountHolderId, "accountHolderId");
    Objects.requireNonNull(accountHolderName, "accountHolderName");
    Objects.requireNonNull(passportNumber, "passportNumber");
    Objects.requireNonNull(dateOfBirth, "dateOfBirth");
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(accountHolderType, "accountHolderType");
    Objects.requireNonNull(createdAt, "createdAt");
  }
}
