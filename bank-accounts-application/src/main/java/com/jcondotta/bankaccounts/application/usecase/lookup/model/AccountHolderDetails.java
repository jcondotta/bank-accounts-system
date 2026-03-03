package com.jcondotta.bankaccounts.application.usecase.lookup.model;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record AccountHolderDetails(
  UUID id,
  PersonalInfoDetails personalInfo,
  ContactInfoDetails contactInfo,
  AddressDetails address,
  AccountHolderType accountHolderType,
  Instant createdAt
) {

  public AccountHolderDetails {
    Objects.requireNonNull(id, "id must not be null");
    Objects.requireNonNull(personalInfo, "personalInfo must not be null");
    Objects.requireNonNull(contactInfo, "contactInfo must not be null");
    Objects.requireNonNull(address, "address must not be null");
    Objects.requireNonNull(accountHolderType, "accountHolderType must not be null");
    Objects.requireNonNull(createdAt, "createdAt must not be null");
  }
}