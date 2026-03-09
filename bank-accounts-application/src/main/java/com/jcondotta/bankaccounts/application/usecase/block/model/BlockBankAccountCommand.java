package com.jcondotta.bankaccounts.application.usecase.block.model;

import com.jcondotta.banking.accounts.domain.bankaccount.identity.BankAccountId;

import java.util.Objects;

public record BlockBankAccountCommand(BankAccountId bankAccountId) {

  public BlockBankAccountCommand {
    Objects.requireNonNull(bankAccountId, "id must not be null");
  }
}
