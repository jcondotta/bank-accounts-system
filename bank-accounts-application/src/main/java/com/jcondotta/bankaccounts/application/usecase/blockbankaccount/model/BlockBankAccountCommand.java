package com.jcondotta.bankaccounts.application.usecase.blockbankaccount.model;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.util.Objects;

public record BlockBankAccountCommand(BankAccountId bankAccountId) {

  public BlockBankAccountCommand {
    Objects.requireNonNull(bankAccountId, "bankAccountId must not be null");
  }

  public static BlockBankAccountCommand of(BankAccountId bankAccountId) {
    return new BlockBankAccountCommand(bankAccountId);
  }
}
