package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.domain.aggregates.AccountHolder;
import org.springframework.stereotype.Component;

@Component
public class AccountHolderDetailsMapperImpl implements AccountHolderDetailsMapper {

    @Override
    public AccountHolderDetails toDetails(AccountHolder accountHolder) {

        if (accountHolder == null) {
            return null;
        }

        return new AccountHolderDetails(
                accountHolder.id(),
                accountHolder.name(),
                accountHolder.passportNumber(),
                accountHolder.dateOfBirth(),
                accountHolder.email(),
                accountHolder.accountHolderType(),
                accountHolder.createdAt()
        );
    }
}
