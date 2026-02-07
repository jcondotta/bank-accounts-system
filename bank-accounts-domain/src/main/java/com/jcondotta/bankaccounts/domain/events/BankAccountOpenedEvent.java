package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.validation.BankAccountValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.DomainEventValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.EventId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;

import java.time.ZonedDateTime;

import static java.util.Objects.requireNonNull;

public record BankAccountOpenedEvent(
  EventId eventId,
  BankAccountId bankAccountId,
  AccountType accountType,
  Currency currency,
  Iban iban,
  AccountStatus status,
  ZonedDateTime occurredAt
) implements BankAccountEvent {

  public BankAccountOpenedEvent {
    requireNonNull(eventId, DomainEventValidationErrors.EVENT_ID_NOT_NULL);
    requireNonNull(bankAccountId, BankAccountValidationErrors.ID_NOT_NULL);
    requireNonNull(accountType, BankAccountValidationErrors.ACCOUNT_TYPE_NOT_NULL);
    requireNonNull(currency, BankAccountValidationErrors.CURRENCY_NOT_NULL);
    requireNonNull(iban, BankAccountValidationErrors.IBAN_NOT_NULL);
    requireNonNull(status, BankAccountValidationErrors.STATUS_NOT_NULL);
    requireNonNull(occurredAt, DomainEventValidationErrors.EVENT_OCCURRED_AT_NOT_NULL);
  }
}