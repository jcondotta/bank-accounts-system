package com.jcondotta.bankaccounts.application.usecase.lookup;


import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.banking.accounts.domain.bankaccount.identity.BankAccountId;

public interface BankAccountLookupUseCase {

    BankAccountDetails lookup(BankAccountId bankAccountId);
}