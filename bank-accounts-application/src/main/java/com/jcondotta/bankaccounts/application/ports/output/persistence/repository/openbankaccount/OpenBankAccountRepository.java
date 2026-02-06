package com.jcondotta.bankaccounts.application.ports.output.persistence.repository.openbankaccount;

import com.jcondotta.bankaccounts.domain.entities.BankAccount;

public interface OpenBankAccountRepository {
    void create(BankAccount bankAccount);
}