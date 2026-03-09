package com.jcondotta.bankaccounts.application.usecase.addholder.model;

import com.jcondotta.banking.accounts.domain.bankaccount.identity.BankAccountId;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.address.Address;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.contact.ContactInfo;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.personal.PersonalInfo;

import java.util.Objects;

public record AddJointAccountHolderCommand(
  BankAccountId bankAccountId,
  PersonalInfo personalInfo,
  ContactInfo contactInfo,
  Address address
) {

  public AddJointAccountHolderCommand {
    Objects.requireNonNull(bankAccountId, "id must not be null");
    Objects.requireNonNull(personalInfo, "personalInfo must not be null");
    Objects.requireNonNull(contactInfo, "contactInfo must not be null");
    Objects.requireNonNull(address, "address must not be null");
  }
}