package com.jcondotta.bankaccounts.application.usecase.openbankaccount.model;

import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public record OpenBankAccountCommand(
  AccountHolderName accountHolderName,
  PassportNumber passportNumber,
  DateOfBirth dateOfBirth,
  AccountType accountType,
  Currency currency
) {

  public OpenBankAccountCommand {
    requireNonNull(accountHolderName, "accountHolderName must not be null");
    requireNonNull(passportNumber, "passportNumber must not be null");
    requireNonNull(dateOfBirth, "dateOfBirth must not be null");
    requireNonNull(accountType, "accountType must not be null");
    requireNonNull(currency, "currency must not be null");
  }

  public static OpenBankAccountCommand of(
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    AccountType accountType,
    Currency currency) {
    return new OpenBankAccountCommand(accountHolderName, passportNumber, dateOfBirth, accountType, currency);
  }
}
