package com.jcondotta.bankaccounts.application.usecase.unblock.model;

import com.jcondotta.banking.accounts.domain.bankaccount.identity.BankAccountId;

import java.util.Objects;

public record UnblockBankAccountCommand(BankAccountId bankAccountId) {

  public UnblockBankAccountCommand {
    Objects.requireNonNull(bankAccountId, "id must not be null");
  }
}
