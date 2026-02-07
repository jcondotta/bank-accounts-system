package com.jcondotta.bankaccounts.application.ports.output.persistence.repository;

import com.jcondotta.bankaccounts.domain.entities.BankAccount;

public interface UpdateBankAccountRepository {

  void update(BankAccount bankAccount);
}