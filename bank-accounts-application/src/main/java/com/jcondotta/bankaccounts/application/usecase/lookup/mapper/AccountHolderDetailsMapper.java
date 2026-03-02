package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.domain.aggregates.AccountHolder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountHolderDetailsMapper {

  @Mapping(target = "id", expression = "java(accountHolder.id().value())")
  @Mapping(target = "firstName", expression = "java(accountHolder.personalInfo().holderName().firstName())")
  @Mapping(target = "lastName", expression = "java(accountHolder.personalInfo().holderName().lastName())")
  @Mapping(target = "documentType", expression = "java(accountHolder.personalInfo().identityDocument().type().name())")
  @Mapping(target = "documentNumber", expression = "java(accountHolder.personalInfo().identityDocument().number().value())")
  @Mapping(target = "dateOfBirth", expression = "java(accountHolder.personalInfo().dateOfBirth().value())")
  @Mapping(target = "email", expression = "java(accountHolder.contactInfo().email().value())")
  @Mapping(target = "phoneNumber", expression = "java(accountHolder.contactInfo().phoneNumber().value())")
  @Mapping(target = "accountHolderType", expression = "java(accountHolder.accountHolderType())")
  @Mapping(target = "createdAt", expression = "java(accountHolder.createdAt())")
  AccountHolderDetails toDetails(AccountHolder accountHolder);
}