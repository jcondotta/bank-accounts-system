package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.BankAccountDetailsResponse;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.model.BankAccountLookupResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  uses = AccountHolderDetailsResponseMapper.class
)
public interface BankAccountLookupResponseControllerMapper {

    @Mapping(target = "bankAccount", source = "bankAccountDetails")
    BankAccountLookupResponse toResponse(BankAccountDetails bankAccountDetails);

    @Mapping(target = "bankAccountId", source = "bankAccountId.value")
    @Mapping(target = "iban", source = "iban.value")
    BankAccountDetailsResponse toBankAccountDetailsResponse(BankAccountDetails bankAccountDetails);

}
