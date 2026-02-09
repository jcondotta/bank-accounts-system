package com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  uses = {
    AccountHolderDetailsMapper.class
  }
)
public interface BankAccountDetailsMapper {

  @Mapping(target = "openingDate", source = "createdAt")
  BankAccountDetails toDetails(BankAccount bankAccount);
}
