package com.jcondotta.bankaccounts.application.usecase.unblockbankaccount.model;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.util.Objects;

public record UnblockBankAccountCommand(BankAccountId bankAccountId) {

  public UnblockBankAccountCommand {
    Objects.requireNonNull(bankAccountId, "bankAccountId must not be null");
  }
}
