package com.jcondotta.bankaccounts.application.usecase.activate.model;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.util.Objects;

public record ActivateBankAccountCommand(BankAccountId bankAccountId) {

  public ActivateBankAccountCommand {
    Objects.requireNonNull(bankAccountId, "id must not be null");
  }
}
