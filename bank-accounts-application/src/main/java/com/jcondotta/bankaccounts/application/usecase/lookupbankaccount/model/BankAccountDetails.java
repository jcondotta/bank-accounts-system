package com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;

import java.time.ZonedDateTime;
import java.util.List;

public record BankAccountDetails (
    BankAccountId bankAccountId,
    AccountType accountType,
    Currency currency,
    Iban iban,
    AccountStatus accountStatus,
    ZonedDateTime dateOfOpening,
    List<AccountHolderDetails> accountHolders
) {
}