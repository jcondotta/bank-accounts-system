package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.aggregates.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
  componentModel = "spring",
  uses = AccountHolderDetailsMapper.class,
  injectionStrategy = org.mapstruct.InjectionStrategy.CONSTRUCTOR
)
public interface BankAccountDetailsMapper {

  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "iban", source = "iban.value")
  @Mapping(target = "accountHolders", source = "accountHolders")
  BankAccountDetails toDetails(BankAccount bankAccount);
}