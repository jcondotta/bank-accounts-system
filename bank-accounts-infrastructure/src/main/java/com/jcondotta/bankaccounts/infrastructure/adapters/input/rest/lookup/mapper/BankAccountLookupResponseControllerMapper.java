package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountLookupResult;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookup.AccountHolderDetailsResponse;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookup.BankAccountDetailsResponse;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookup.model.BankAccountLookupResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountLookupResponseControllerMapper {

    default BankAccountLookupResponse toResponse(BankAccountLookupResult result){
        BankAccountDetailsResponse bankAccountDetailsResponse = new BankAccountDetailsResponse(
            result.bankAccountDetails().bankAccountId().value(),
            result.bankAccountDetails().accountType(),
            result.bankAccountDetails().currency(),
            result.bankAccountDetails().iban().value(),
            result.bankAccountDetails().dateOfOpening(),
            result.bankAccountDetails().accountStatus(),
            result.bankAccountDetails().accountHolders()
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
