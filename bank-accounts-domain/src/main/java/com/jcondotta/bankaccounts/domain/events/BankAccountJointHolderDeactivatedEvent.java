package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.validation.DomainEventValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.domain.events.DomainEvent;
import com.jcondotta.domain.identity.EventId;

import java.time.Instant;

import static com.jcondotta.domain.support.DomainPreconditions.required;

public record BankAccountJointHolderDeactivatedEvent(
  EventId eventId,
  BankAccountId aggregateId,
  AccountHolderId accountHolderId,
  Instant occurredAt
) implements DomainEvent<BankAccountId> {

  public BankAccountJointHolderDeactivatedEvent {
    required(eventId, DomainEventValidationErrors.EVENT_ID_NOT_NULL);
    required(aggregateId, DomainEventValidationErrors.AGGREGATE_ID_NOT_NULL);
    required(accountHolderId, AccountHolderId.ACCOUNT_HOLDER_ID_NOT_PROVIDED);
    required(occurredAt, DomainEventValidationErrors.EVENT_OCCURRED_AT_NOT_NULL);
  }
}
