package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountDetails;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.AccountHolderDetailsResponse;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.BankAccountDetailsResponse;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.model.BankAccountLookupResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountLookupResponseControllerMapper {

    default BankAccountLookupResponse toResponse(BankAccountDetails bankAccountDetails){
        BankAccountDetailsResponse bankAccountDetailsResponse = new BankAccountDetailsResponse(
            bankAccountDetails.bankAccountId().value(),
            bankAccountDetails.accountType(),
            bankAccountDetails.currency(),
            bankAccountDetails.iban().value(),
            bankAccountDetails.openingDate(),
            bankAccountDetails.accountStatus(),
            bankAccountDetails.accountHolders()
              .stream()
              .map(accountHolderDetails -> new AccountHolderDetailsResponse(
                  accountHolderDetails.accountHolderId().value(),
                  accountHolderDetails.accountHolderName().value(),
                  accountHolderDetails.passportNumber().value(),
                  accountHolderDetails.dateOfBirth().value(),
                  accountHolderDetails.accountHolderType(),
                  accountHolderDetails.createdAt()
              )).toList()
        );

        return new BankAccountLookupResponse(bankAccountDetailsResponse);
    }
}
