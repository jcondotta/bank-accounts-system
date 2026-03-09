package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.banking.accounts.domain.bankaccount.aggregate.AccountHolder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
  uses = {
    PersonalInfoDetailsMapper.class,
    ContactInfoDetailsMapper.class,
    AddressDetailsMapper.class
  },
  injectionStrategy = org.mapstruct.InjectionStrategy.CONSTRUCTOR)
public interface AccountHolderDetailsMapper {
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "personalInfo", source = "personalInfo")
  @Mapping(target = "contactInfo", source = "contactInfo")
  @Mapping(target = "address", source = "address")
  @Mapping(target = "type", source = "accountHolderType")
  @Mapping(target = "createdAt", source = "createdAt")
  AccountHolderDetails toDetails(AccountHolder accountHolder);
}