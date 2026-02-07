package com.jcondotta.bankaccounts.application.ports.output.persistence.repository;

import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.util.Optional;

public interface LookupBankAccountRepository {

  Optional<BankAccount> byId(BankAccountId bankAccountId);
}