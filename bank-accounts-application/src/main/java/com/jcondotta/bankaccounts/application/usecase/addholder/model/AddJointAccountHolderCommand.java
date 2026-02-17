package com.jcondotta.bankaccounts.application.usecase.addholder.model;

import com.jcondotta.bankaccounts.domain.value_objects.*;

import java.util.Objects;

public record AddJointAccountHolderCommand(
  BankAccountId bankAccountId,
  AccountHolderName accountHolderName,
  PassportNumber passportNumber,
  DateOfBirth dateOfBirth,
  Email email
) {

  public AddJointAccountHolderCommand {
    Objects.requireNonNull(bankAccountId, "bankAccountId must not be null");
    Objects.requireNonNull(accountHolderName, "accountHolderName must not be null");
    Objects.requireNonNull(passportNumber, "passportNumber must not be null");
    Objects.requireNonNull(dateOfBirth, "dateOfBirth must not be null");
    Objects.requireNonNull(email, "email must not be null");
  }
}


