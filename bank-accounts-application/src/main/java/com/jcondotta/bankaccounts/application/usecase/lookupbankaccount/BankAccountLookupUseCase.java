package com.jcondotta.bankaccounts.application.usecase.lookupbankaccount;


import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountLookupResult;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

public interface BankAccountLookupUseCase {

    BankAccountLookupResult lookup(BankAccountId bankAccountId);
}