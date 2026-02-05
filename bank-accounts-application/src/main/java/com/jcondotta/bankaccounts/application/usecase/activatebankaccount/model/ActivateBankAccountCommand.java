package com.jcondotta.bankaccounts.application.usecase.activatebankaccount.model;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.util.Objects;

public record ActivateBankAccountCommand(BankAccountId bankAccountId) {

  public ActivateBankAccountCommand {
    Objects.requireNonNull(bankAccountId, "bankAccountId must not be null");
  }

  public static ActivateBankAccountCommand of(BankAccountId bankAccountId) {
    return new ActivateBankAccountCommand(bankAccountId);
  }
}
