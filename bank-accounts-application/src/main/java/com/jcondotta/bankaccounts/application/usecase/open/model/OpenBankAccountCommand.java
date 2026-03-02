package com.jcondotta.bankaccounts.application.usecase.open.model;

import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.address.Address;
import com.jcondotta.bankaccounts.domain.value_objects.contact.ContactInfo;
import com.jcondotta.bankaccounts.domain.value_objects.personal.PersonalInfo;

import static java.util.Objects.requireNonNull;

public record OpenBankAccountCommand(
  PersonalInfo personalInfo,
  ContactInfo contactInfo,
  Address address,
  AccountType accountType,
  Currency currency
) {

  public OpenBankAccountCommand {
    requireNonNull(personalInfo, "personalInfo must not be null");
    requireNonNull(contactInfo, "contactInfo must not be null");
    requireNonNull(address, "address must not be null");
    requireNonNull(accountType, "accountType must not be null");
    requireNonNull(currency, "currency must not be null");
  }
}