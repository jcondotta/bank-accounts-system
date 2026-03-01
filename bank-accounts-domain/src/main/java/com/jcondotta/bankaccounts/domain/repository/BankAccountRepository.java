package com.jcondotta.bankaccounts.domain.repository;

import com.jcondotta.bankaccounts.domain.aggregates.BankAccount;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.util.Optional;

public interface BankAccountRepository {

  Optional<BankAccount> findById(BankAccountId id);
  void save(BankAccount bankAccount);

}