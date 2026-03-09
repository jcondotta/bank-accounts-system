package com.jcondotta.bankaccounts.application.usecase.activate.model;

import com.jcondotta.banking.accounts.domain.bankaccount.identity.BankAccountId;

import java.util.Objects;

public record ActivateBankAccountCommand(BankAccountId bankAccountId) {

  public ActivateBankAccountCommand {
    Objects.requireNonNull(bankAccountId, "id must not be null");
  }
}
