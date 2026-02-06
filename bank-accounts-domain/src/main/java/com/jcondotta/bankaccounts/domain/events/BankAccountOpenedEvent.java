package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;

import java.time.ZonedDateTime;

import static java.util.Objects.requireNonNull;

public record BankAccountOpenedEvent(
  BankAccountId bankAccountId,
  AccountType accountType,
  Currency currency,
  Iban iban,
  AccountStatus status,
  AccountHolderId primaryAccountHolderId,
  ZonedDateTime occurredAt
) implements BankAccountEvent {

  public BankAccountOpenedEvent {
    requireNonNull(bankAccountId, "bankAccountId must not be null");
    requireNonNull(accountType, "accountType must not be null");
    requireNonNull(currency, "currency must not be null");
    requireNonNull(iban, "iban must not be null");
    requireNonNull(status, "status must not be null");
    requireNonNull(primaryAccountHolderId, "primaryAccountHolderId must not be null");
    requireNonNull(occurredAt, "occurredAt must not be null");
  }
}