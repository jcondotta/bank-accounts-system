package com.jcondotta.bankaccounts.application.usecase.lookup.model;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record BankAccountDetails(
  UUID id,
  AccountType accountType,
  Currency currency,
  String iban,
  AccountStatus accountStatus,
  Instant createdAt,
  List<AccountHolderDetails> accountHolders
) {

  public BankAccountDetails {
    requireNonNull(id, "id must not be null");
    requireNonNull(accountType, "accountType must not be null");
    requireNonNull(currency, "currency must not be null");
    requireNonNull(iban, "iban must not be null");
    requireNonNull(accountStatus, "accountStatus must not be null");
    requireNonNull(createdAt, "createdAt must not be null");

    requireNonNull(accountHolders, "accountHolders must not be null");
    accountHolders = List.copyOf(accountHolders);
  }
}