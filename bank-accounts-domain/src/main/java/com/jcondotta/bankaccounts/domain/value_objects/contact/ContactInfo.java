package com.jcondotta.bankaccounts.domain.value_objects.contact;

import static com.jcondotta.bankaccounts.domain.validation.DomainPreconditions.required;

public record ContactInfo(Email email, PhoneNumber phoneNumber) {

  public static final String EMAIL_NOT_PROVIDED = "Email must be provided.";
  public static final String PHONE_NUMBER_NOT_PROVIDED = "Phone number must be provided.";

  public ContactInfo {
    required(email, EMAIL_NOT_PROVIDED);
    required(phoneNumber, PHONE_NUMBER_NOT_PROVIDED);
  }

  public static ContactInfo of(Email email, PhoneNumber phoneNumber) {
    return new ContactInfo(email, phoneNumber);
  }
}