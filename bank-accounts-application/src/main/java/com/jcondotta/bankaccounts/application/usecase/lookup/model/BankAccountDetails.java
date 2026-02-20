package com.jcondotta.bankaccounts.application.usecase.lookup.model;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;

import java.time.Instant;
import java.util.List;

import static java.util.Objects.requireNonNull;

public record BankAccountDetails(
  BankAccountId bankAccountId,
  AccountType accountType,
  Currency currency,
  Iban iban,
  AccountStatus accountStatus,
  Instant createdAt,
  List<AccountHolderDetails> accountHolders
) {
  public BankAccountDetails {
    requireNonNull(bankAccountId, "bankAccountId");
    requireNonNull(accountType, "accountType");
    requireNonNull(currency, "currency");
    requireNonNull(iban, "iban");
    requireNonNull(accountStatus, "accountStatus");
    requireNonNull(createdAt, "createdAt");
    requireNonNull(accountHolders, "accountHolders");
  }
}