package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.domain.aggregates.AccountHolder;

public interface AccountHolderDetailsMapper {

    AccountHolderDetails toDetails(AccountHolder accountHolder);
}
