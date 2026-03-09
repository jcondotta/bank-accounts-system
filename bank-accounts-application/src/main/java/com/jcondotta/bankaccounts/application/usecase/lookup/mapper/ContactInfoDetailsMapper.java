package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.ContactInfoDetails;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.contact.ContactInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactInfoDetailsMapper {

  default ContactInfoDetails toDetails(ContactInfo contactInfo) {
    if (contactInfo == null) {
      return null;
    }

    return new ContactInfoDetails(
      contactInfo.email().value() ,
      contactInfo.phoneNumber().value()
    );
  }
}