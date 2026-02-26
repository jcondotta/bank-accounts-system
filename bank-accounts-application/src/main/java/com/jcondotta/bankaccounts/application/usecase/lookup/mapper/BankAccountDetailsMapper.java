package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.aggregates.BankAccount;

public interface BankAccountDetailsMapper {

  BankAccountDetails toDetails(BankAccount bankAccount);
}