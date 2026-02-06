package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.value_objects.*;

import java.time.ZonedDateTime;

import static java.util.Objects.requireNonNull;

public record JointAccountHolderAddedEvent(
  BankAccountId bankAccountId,
  AccountHolderId accountHolderId,
  AccountHolderName name,
  PassportNumber passportNumber,
  DateOfBirth dateOfBirth,
  ZonedDateTime occurredAt
) implements BankAccountEvent {

  public JointAccountHolderAddedEvent {
    requireNonNull(bankAccountId, "bankAccountId must not be null");
    requireNonNull(accountHolderId, "accountHolderId must not be null");
    requireNonNull(name, "name must not be null");
    requireNonNull(passportNumber, "passportNumber must not be null");
    requireNonNull(dateOfBirth, "dateOfBirth must not be null");
    requireNonNull(occurredAt, "occurredAt must not be null");
  }
}
