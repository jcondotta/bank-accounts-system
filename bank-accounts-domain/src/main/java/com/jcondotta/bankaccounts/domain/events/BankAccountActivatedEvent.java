package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.time.ZonedDateTime;

import static java.util.Objects.requireNonNull;

public record BankAccountActivatedEvent(
  BankAccountId bankAccountId,
  ZonedDateTime occurredAt
) implements BankAccountEvent {

  public BankAccountActivatedEvent {
    requireNonNull(bankAccountId, "bankAccountId must not be null");
    requireNonNull(occurredAt, "occurredAt must not be null");
  }
}
