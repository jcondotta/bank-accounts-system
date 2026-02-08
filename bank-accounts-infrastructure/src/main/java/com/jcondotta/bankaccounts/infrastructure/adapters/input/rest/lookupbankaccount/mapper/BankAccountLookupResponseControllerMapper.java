package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.AccountHolderDetailsResponse;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.BankAccountDetailsResponse;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.model.BankAccountLookupResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountLookupResponseControllerMapper {

    default BankAccountLookupResponse toResponse(BankAccountLookupResult result){
        BankAccountDetailsResponse bankAccountDetailsResponse = new BankAccountDetailsResponse(
            result.bankAccountDetails().bankAccountId().value(),
            result.bankAccountDetails().accountType(),
            result.bankAccountDetails().currency(),
            result.bankAccountDetails().iban().value(),
            result.bankAccountDetails().openingDate(),
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
