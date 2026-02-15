package com.jcondotta.bankaccounts.application.usecase.closebankaccount.model;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.util.Objects;

public record CloseBankAccountCommand(BankAccountId bankAccountId) {

  public CloseBankAccountCommand {
    Objects.requireNonNull(bankAccountId, "bankAccountId must not be null");
  }
}
