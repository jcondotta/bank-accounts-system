package com.jcondotta.bankaccounts.application.ports.output.repository.openbankaccount;

import com.jcondotta.bankaccounts.domain.entities.BankAccount;

public interface OpenBankAccountRepository {
    void save(BankAccount bankAccount);
}