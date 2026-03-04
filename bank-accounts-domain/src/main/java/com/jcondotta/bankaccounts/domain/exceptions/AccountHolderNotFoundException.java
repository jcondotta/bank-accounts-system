package com.jcondotta.bankaccounts.domain.exceptions;

import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.domain.exception.DomainNotFoundException;

public class AccountHolderNotFoundException extends DomainNotFoundException {

  public AccountHolderNotFoundException(AccountHolderId accountHolderId) {
    super("Account holder not found with id: " + accountHolderId.value());
  }
}