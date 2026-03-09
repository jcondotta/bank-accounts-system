package com.jcondotta.bankaccounts.application.usecase.close.model;

import com.jcondotta.banking.accounts.domain.bankaccount.identity.BankAccountId;

import java.util.Objects;

public record CloseBankAccountCommand(BankAccountId bankAccountId) {

  public CloseBankAccountCommand {
    Objects.requireNonNull(bankAccountId, "id must not be null");
  }
}
