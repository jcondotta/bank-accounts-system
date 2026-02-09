package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.AccountHolderDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountHolderDetailsResponseMapper {

  @Mapping(target = "accountHolderId", source = "accountHolderId.value")
  @Mapping(target = "accountHolderName", source = "accountHolderName.value")
  @Mapping(target = "passportNumber", source = "passportNumber.value")
  @Mapping(target = "dateOfBirth", source = "dateOfBirth.value")
  AccountHolderDetailsResponse toResponse(AccountHolderDetails details);
}
