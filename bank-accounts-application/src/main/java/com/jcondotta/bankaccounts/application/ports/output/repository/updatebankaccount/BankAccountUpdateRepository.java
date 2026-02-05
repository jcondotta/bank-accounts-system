package com.jcondotta.bankaccounts.application.ports.output.repository.updatebankaccount;

import com.jcondotta.bankaccounts.domain.entities.BankAccount;

public interface BankAccountUpdateRepository {

  void save(BankAccount bankAccount);
}