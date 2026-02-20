package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;

public interface BankAccountDetailsMapper {

  BankAccountDetails toDetails(BankAccount bankAccount);
}