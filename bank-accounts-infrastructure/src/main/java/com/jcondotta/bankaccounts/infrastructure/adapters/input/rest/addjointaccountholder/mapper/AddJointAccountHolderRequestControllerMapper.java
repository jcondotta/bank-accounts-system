package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.mapper;

import com.jcondotta.bankaccounts.application.usecase.addholder.model.AddJointAccountHolderCommand;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.model.AddJointAccountHolderRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.common.AddressRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.common.ContactInfoRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.common.PersonalInfoRequest;
import com.jcondotta.banking.accounts.domain.bankaccount.enums.DocumentCountry;
import com.jcondotta.banking.accounts.domain.bankaccount.enums.DocumentType;
import com.jcondotta.banking.accounts.domain.bankaccount.identity.BankAccountId;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.address.*;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.contact.ContactInfo;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.contact.Email;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.contact.PhoneNumber;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.personal.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddJointAccountHolderRequestControllerMapper {

  @Mapping(target = "aggregateId", source = "id", qualifiedByName = "toBankAccountId")
  @Mapping(target = "personalInfo", source = "request.personalInfo", qualifiedByName = "toPersonalInfo")
  @Mapping(target = "contactInfo", source = "request.contactInfo", qualifiedByName = "toContactInfo")
  @Mapping(target = "address", source = "request.address", qualifiedByName = "toAddress")
  AddJointAccountHolderCommand toCommand(UUID id, AddJointAccountHolderRequest request);

  @Named("toBankAccountId")
  default BankAccountId toBankAccountId(UUID value) {
    return BankAccountId.of(value);
  }

  @Named("toPersonalInfo")
  default PersonalInfo toPersonalInfo(PersonalInfoRequest value) {
    if (value == null) return null;

    return PersonalInfo.of(
      AccountHolderName.of(value.firstName(), value.lastName()),
      IdentityDocument.of(
        DocumentCountry.valueOf(value.identityDocument().country()),
        DocumentType.valueOf(value.identityDocument().type()),
        DocumentNumber.of(value.identityDocument().number())
      ),
      DateOfBirth.of(value.dateOfBirth())
    );
  }

  @Named("toContactInfo")
  default ContactInfo toContactInfo(ContactInfoRequest value) {
    if (value == null) return null;

    return ContactInfo.of(
      Email.of(value.email()),
      PhoneNumber.of(value.phoneNumber())
    );
  }

  @Named("toAddress")
  default Address toAddress(AddressRequest value) {
    if (value == null) return null;

    return new Address(
      Street.of(value.street()),
      StreetNumber.of(value.streetNumber()),
      AddressComplement.of(value.complement()),
      PostalCode.of(value.postalCode()),
      City.of(value.city())
    );
  }
}
