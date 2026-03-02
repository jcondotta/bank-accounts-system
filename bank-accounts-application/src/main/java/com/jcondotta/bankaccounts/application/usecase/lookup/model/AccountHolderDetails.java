package com.jcondotta.bankaccounts.application.usecase.lookup.model;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record AccountHolderDetails(
  UUID id,
  String firstName,
  String lastName,
  String documentType,
  String documentNumber,
  LocalDate dateOfBirth,
  String email,
  String phoneNumber,
  AccountHolderType accountHolderType,
  Instant createdAt
) {

  public AccountHolderDetails {
    Objects.requireNonNull(id, "id must not be null");
    Objects.requireNonNull(firstName, "firstName must not be null");
    Objects.requireNonNull(lastName, "lastName must not be null");
    Objects.requireNonNull(documentType, "documentType must not be null");
    Objects.requireNonNull(documentNumber, "documentNumber must not be null");
    Objects.requireNonNull(dateOfBirth, "dateOfBirth must not be null");
    Objects.requireNonNull(email, "email must not be null");
    Objects.requireNonNull(phoneNumber, "phoneNumber must not be null");
    Objects.requireNonNull(accountHolderType, "accountHolderType must not be null");
    Objects.requireNonNull(createdAt, "createdAt must not be null");
  }
}
