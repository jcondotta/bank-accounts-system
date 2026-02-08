package com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.domain.entities.AccountHolder;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountHolderDetailsMapper {

    AccountHolderDetails toDetails(AccountHolder accountHolder);
}
