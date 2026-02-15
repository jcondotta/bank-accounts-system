package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.events.types.DomainEventType;
import com.jcondotta.bankaccounts.domain.validation.BankAccountValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.DomainEventValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.EventId;

import java.time.ZonedDateTime;

import static java.util.Objects.requireNonNull;

public record BankAccountUnblockedEvent(
  EventId eventId,
  BankAccountId bankAccountId,
  ZonedDateTime occurredAt
) implements BankAccountEvent {

  public BankAccountUnblockedEvent {
    requireNonNull(eventId, DomainEventValidationErrors.EVENT_ID_NOT_NULL);
    requireNonNull(bankAccountId, BankAccountValidationErrors.ID_NOT_NULL);
    requireNonNull(occurredAt, DomainEventValidationErrors.EVENT_OCCURRED_AT_NOT_NULL);
  }

  @Override
  public DomainEventType eventType() {
    return DomainEventType.BANK_ACCOUNT_UNBLOCKED;
  }
}
