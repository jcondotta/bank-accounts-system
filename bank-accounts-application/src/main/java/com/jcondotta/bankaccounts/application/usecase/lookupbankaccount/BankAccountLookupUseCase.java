package com.jcondotta.bankaccounts.application.usecase.lookupbankaccount;


import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

public interface BankAccountLookupUseCase {

    BankAccountDetails lookup(BankAccountId bankAccountId);
}