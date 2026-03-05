package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.validation.BankAccountValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.DomainEventValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.domain.events.DomainEvent;
import com.jcondotta.domain.identity.EventId;

import java.time.Instant;

import static com.jcondotta.domain.support.DomainPreconditions.required;

public record BankAccountOpenedEvent(
  EventId eventId,
  BankAccountId aggregateId,
  AccountType accountType,
  Currency currency,
  AccountHolderId primaryAccountHolderId,
  Instant occurredAt
) implements DomainEvent<BankAccountId> {

  public BankAccountOpenedEvent {
    required(eventId, DomainEventValidationErrors.EVENT_ID_NOT_NULL);
    required(aggregateId, DomainEventValidationErrors.AGGREGATE_ID_NOT_NULL);
    required(accountType, BankAccountValidationErrors.ACCOUNT_TYPE_NOT_NULL);
    required(currency, BankAccountValidationErrors.CURRENCY_NOT_NULL);
    required(primaryAccountHolderId, AccountHolderId.ACCOUNT_HOLDER_ID_NOT_PROVIDED);
    required(occurredAt, DomainEventValidationErrors.EVENT_OCCURRED_AT_NOT_NULL);
  }
}