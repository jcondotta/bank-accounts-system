package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.validation.DomainEventValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.domain.events.DomainEvent;
import com.jcondotta.domain.identity.EventId;

import java.time.Instant;

import static com.jcondotta.domain.support.DomainPreconditions.required;
import static java.util.Objects.requireNonNull;

public record BankAccountBlockedEvent(
  EventId eventId,
  BankAccountId aggregateId,
  Instant occurredAt
) implements DomainEvent<BankAccountId> {

  public BankAccountBlockedEvent {
    required(eventId, DomainEventValidationErrors.EVENT_ID_NOT_NULL);
    required(aggregateId, DomainEventValidationErrors.AGGREGATE_ID_NOT_NULL);
    required(occurredAt, DomainEventValidationErrors.EVENT_OCCURRED_AT_NOT_NULL);
  }
}
