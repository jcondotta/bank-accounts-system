package com.jcondotta.bankaccounts.application.usecase.openbankaccount.model;

import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;

public record OpenBankAccountCommand(
  AccountHolderName accountHolderName,
  PassportNumber passportNumber,
  DateOfBirth dateOfBirth,
  AccountType accountType,
  Currency currency
) {
  public static OpenBankAccountCommand of(
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    AccountType accountType,
    Currency currency) {
    return new OpenBankAccountCommand(accountHolderName, passportNumber, dateOfBirth, accountType, currency);
  }
}
