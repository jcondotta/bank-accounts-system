package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.BankAccountDetailsResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  uses = AccountHolderDetailsResponseMapper.class
)
public interface BankAccountLookupResponseControllerMapper {

    BankAccountDetailsResponse toBankAccountDetailsResponse(BankAccountDetails bankAccountDetails);
}
