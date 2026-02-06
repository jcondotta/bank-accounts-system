package com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountDetails;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountLookupResult;
import com.jcondotta.bankaccounts.domain.entities.AccountHolder;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountLookupResultMapper {

    default BankAccountLookupResult toResult(BankAccount bankAccount) {
        return new BankAccountLookupResult(toBankAccountDetails(bankAccount));
    }

    default BankAccountDetails toBankAccountDetails(BankAccount bankAccount) {
        return new BankAccountDetails(
            bankAccount.getBankAccountId(),
            bankAccount.getAccountType(),
            bankAccount.getCurrency(),
            bankAccount.getIban(),
            bankAccount.getStatus(),
            bankAccount.getCreatedAt(),
            bankAccount.getAccountHolders().stream()
                .map(this::toAccountHolderDetails)
                .toList()
        );
    }

    default AccountHolderDetails toAccountHolderDetails(AccountHolder accountHolder) {
        return new AccountHolderDetails(
            accountHolder.getAccountHolderId(),
            accountHolder.getAccountHolderName(),
            accountHolder.getPassportNumber(),
            accountHolder.getDateOfBirth(),
            accountHolder.getAccountHolderType(),
            accountHolder.getCreatedAt()
        );
    }
}
