package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.mapper;

import com.jcondotta.bankaccounts.application.usecase.addholder.model.AddJointAccountHolderCommand;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.model.AddJointAccountHolderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddJointAccountHolderRequestControllerMapper {

  @Mapping(target = "bankAccountId", source = "bankAccountId", qualifiedByName = "toBankAccountId")
  @Mapping(target = "name", source = "request.name", qualifiedByName = "toAccountHolderName")
  @Mapping(target = "passportNumber", source = "request.passportNumber", qualifiedByName = "toPassportNumber")
  @Mapping(target = "dateOfBirth", source = "request.dateOfBirth", qualifiedByName = "toDateOfBirth")
  @Mapping(target = "email", source = "request.email", qualifiedByName = "toEmail")
  AddJointAccountHolderCommand toCommand(UUID bankAccountId, AddJointAccountHolderRequest request);

  @Named("toBankAccountId")
  default BankAccountId toBankAccountId(UUID value) {
    return BankAccountId.of(value);
  }

  @Named("toAccountHolderName")
  default AccountHolderName toAccountHolderName(String value) {
    return AccountHolderName.of(value);
  }

  @Named("toPassportNumber")
  default PassportNumber toPassportNumber(String value) {
    return PassportNumber.of(value);
  }

  @Named("toDateOfBirth")
  default DateOfBirth toDateOfBirth(LocalDate value) {
    return DateOfBirth.of(value);
  }

  @Named("toEmail")
  default Email toEmail(String value) {
    return Email.of(value);
  }
}
