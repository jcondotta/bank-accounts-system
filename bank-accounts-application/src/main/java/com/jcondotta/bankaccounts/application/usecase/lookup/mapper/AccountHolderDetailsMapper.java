package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.domain.entities.AccountHolder;
import org.mapstruct.Mapper;

public interface AccountHolderDetailsMapper {

    AccountHolderDetails toDetails(AccountHolder accountHolder);
}
