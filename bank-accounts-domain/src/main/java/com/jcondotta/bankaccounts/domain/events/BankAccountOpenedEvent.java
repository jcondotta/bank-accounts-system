package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.aggregates.AccountHolder;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.types.DomainEventType;
import com.jcondotta.bankaccounts.domain.validation.BankAccountValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.DomainEventValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.EventId;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public record BankAccountOpenedEvent(
  EventId eventId,
  BankAccountId bankAccountId,
  AccountType accountType,
  Currency currency,
  AccountHolderId primaryAccountHolderId,
  Instant occurredAt
) implements BankAccountEvent {

  public BankAccountOpenedEvent {
    requireNonNull(eventId, DomainEventValidationErrors.EVENT_ID_NOT_NULL);
    requireNonNull(bankAccountId, BankAccountValidationErrors.ID_NOT_NULL);
    requireNonNull(accountType, BankAccountValidationErrors.ACCOUNT_TYPE_NOT_NULL);
    requireNonNull(currency, BankAccountValidationErrors.CURRENCY_NOT_NULL);
    requireNonNull(primaryAccountHolderId, AccountHolderId.ACCOUNT_HOLDER_ID_NOT_PROVIDED);
    requireNonNull(occurredAt, DomainEventValidationErrors.EVENT_OCCURRED_AT_NOT_NULL);
  }

  @Override
  public DomainEventType eventType() {
    return DomainEventType.BANK_ACCOUNT_OPENED;
  }
}