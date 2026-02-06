package com.jcondotta.bankaccounts.application.ports.output.persistence.repository.updatebankaccount;

import com.jcondotta.bankaccounts.domain.entities.BankAccount;

public interface BankAccountUpdateRepository {

  void save(BankAccount bankAccount);
}