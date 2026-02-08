package com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.entities.AccountHolder;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
  nullValueMappingStrategy = org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT)
public interface BankAccountDetailsMapper {

    @Mapping(target = "bankAccountId", source = "bankAccountId")
    @Mapping(target = "accountType", source = "accountType")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "iban", source = "iban")
    @Mapping(target = "accountStatus", source = "status")
    @Mapping(target = "openingDate", source = "createdAt")
    @Mapping(target = "accountHolders", source = "accountHolders")
    BankAccountDetails toDetails(BankAccount bankAccount);

    @IterableMapping(elementTargetType = AccountHolderDetails.class)
    List<AccountHolderDetails> toAccountHolderDetailsList(List<AccountHolder> accountHolders);
}
