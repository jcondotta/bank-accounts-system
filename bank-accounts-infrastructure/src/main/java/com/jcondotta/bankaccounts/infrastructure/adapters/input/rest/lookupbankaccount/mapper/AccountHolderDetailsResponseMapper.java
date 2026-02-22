package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.AccountHolderDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountHolderDetailsResponseMapper {

  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "name", source = "name.value")
  @Mapping(target = "passportNumber", source = "passportNumber.value")
  @Mapping(target = "dateOfBirth", source = "dateOfBirth.value")
  @Mapping(target = "email", source = "email.value")
  AccountHolderDetailsResponse toResponse(AccountHolderDetails details);
}
