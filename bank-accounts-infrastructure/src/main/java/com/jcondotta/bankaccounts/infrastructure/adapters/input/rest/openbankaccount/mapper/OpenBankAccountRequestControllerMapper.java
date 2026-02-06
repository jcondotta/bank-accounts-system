package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.openbankaccount.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.OpenBankAccountRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.PrimaryAccountHolderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OpenBankAccountRequestControllerMapper {

    default OpenBankAccountCommand toCommand(OpenBankAccountRequest request) {
        var primaryAccountHolderRequest = toPrimaryAccountHolderRequest(request.accountHolder());
        return new OpenBankAccountCommand(
          AccountHolderName.of(primaryAccountHolderRequest.accountHolderName()),
          PassportNumber.of(primaryAccountHolderRequest.passportNumber()),
          DateOfBirth.of(primaryAccountHolderRequest.dateOfBirth()),
          request.accountType(),
          request.currency()
        );
    }

    private PrimaryAccountHolderRequest toPrimaryAccountHolderRequest(PrimaryAccountHolderRequest request) {
        return PrimaryAccountHolderRequest.of(request.accountHolderName(), request.dateOfBirth(), request.passportNumber());
    }
}
