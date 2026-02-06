package com.jcondotta.bankaccounts.application.ports.output.persistence.repository.lookupbankaccount;

import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.util.Optional;

public interface BankAccountLookupRepository {
    Optional<BankAccount> byId(BankAccountId bankAccountId);
}