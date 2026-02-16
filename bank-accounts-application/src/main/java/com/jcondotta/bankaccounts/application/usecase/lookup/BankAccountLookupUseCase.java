package com.jcondotta.bankaccounts.application.usecase.lookup;


import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

public interface BankAccountLookupUseCase {

    BankAccountDetails lookup(BankAccountId bankAccountId);
}