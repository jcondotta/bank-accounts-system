package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.aggregates.BankAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankAccountDetailsMapperImpl implements BankAccountDetailsMapper {

  private final AccountHolderDetailsMapper accountHolderDetailsMapper;

  @Override
  public BankAccountDetails toDetails(BankAccount bankAccount) {
    if (bankAccount == null) {
      return null;
    }

    return new BankAccountDetails(
      bankAccount.id(),
      bankAccount.accountType(),
      bankAccount.currency(),
      bankAccount.iban(),
      bankAccount.accountStatus(),
      bankAccount.createdAt(),
      bankAccount.accountHolders()
        .stream()
        .map(accountHolderDetailsMapper::toDetails)
        .toList()
    );
  }
}
