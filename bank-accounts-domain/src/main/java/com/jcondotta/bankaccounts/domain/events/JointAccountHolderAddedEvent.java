package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.events.types.DomainEventType;
import com.jcondotta.bankaccounts.domain.validation.BankAccountValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.DomainEventValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.EventId;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public record JointAccountHolderAddedEvent(
  EventId eventId,
  BankAccountId bankAccountId,
  AccountHolderId accountHolderId,
  Instant occurredAt
) implements BankAccountEvent {

  public JointAccountHolderAddedEvent {
    requireNonNull(eventId, DomainEventValidationErrors.EVENT_ID_NOT_NULL);
    requireNonNull(bankAccountId, BankAccountValidationErrors.ID_NOT_NULL);
    requireNonNull(accountHolderId, AccountHolderId.ACCOUNT_HOLDER_ID_NOT_PROVIDED);
    requireNonNull(occurredAt, DomainEventValidationErrors.EVENT_OCCURRED_AT_NOT_NULL);
  }

  @Override
  public DomainEventType eventType() {
    return DomainEventType.JOINT_ACCOUNT_HOLDER_ADDED;
  }
}
