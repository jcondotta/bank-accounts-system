package com.jcondotta.bankaccounts.application.ports.output;

import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;

public interface IbanGenerator {

    Iban generate(AccountType accountType, Currency currency);
}