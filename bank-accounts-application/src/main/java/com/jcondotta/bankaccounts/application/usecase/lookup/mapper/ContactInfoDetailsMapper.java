package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.ContactInfoDetails;
import com.jcondotta.bankaccounts.domain.value_objects.contact.ContactInfo;
import org.springframework.stereotype.Component;

@Component
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