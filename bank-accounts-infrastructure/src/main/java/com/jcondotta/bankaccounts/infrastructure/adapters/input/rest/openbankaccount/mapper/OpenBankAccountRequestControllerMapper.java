package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.open.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.Email;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.OpenBankAccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OpenBankAccountRequestControllerMapper {

    @Mapping(target = "name", source = "accountHolder.name", qualifiedByName = "toAccountHolderName")
    @Mapping(target = "passportNumber", source = "accountHolder.passportNumber", qualifiedByName = "toPassportNumber")
    @Mapping(target = "dateOfBirth", source = "accountHolder.dateOfBirth", qualifiedByName = "toDateOfBirth")
    @Mapping(target = "email", source = "accountHolder.email", qualifiedByName = "toEmail")
    @Mapping(target = "accountType", source = "accountType")
    @Mapping(target = "currency", source = "currency")
    OpenBankAccountCommand toCommand(OpenBankAccountRequest request);

    @Named("toAccountHolderName")
    default AccountHolderName toAccountHolderName(String value) {
        return AccountHolderName.of(value);
    }

    @Named("toPassportNumber")
    default PassportNumber toPassportNumber(String value) {
        return PassportNumber.of(value);
    }

    @Named("toDateOfBirth")
    default DateOfBirth toDateOfBirth(java.time.LocalDate value) {
        return DateOfBirth.of(value);
    }

    @Named("toEmail")
    default Email toEmail(String value) {
        return Email.of(value);
    }
}
