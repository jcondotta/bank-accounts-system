package com.jcondotta.bankaccounts.domain.exceptions;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

public class BankAccountNotFoundException extends DomainObjectNotFoundException {

  public BankAccountNotFoundException(BankAccountId bankAccountId) {
    super("Bank account not found with id: " + bankAccountId.value());
  }
}