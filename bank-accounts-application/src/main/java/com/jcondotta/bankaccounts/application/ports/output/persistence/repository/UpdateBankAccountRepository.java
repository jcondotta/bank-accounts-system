package com.jcondotta.bankaccounts.application.ports.output.persistence.repository;

import com.jcondotta.bankaccounts.domain.aggregates.BankAccount;

public interface UpdateBankAccountRepository {

  void update(BankAccount bankAccount);
}