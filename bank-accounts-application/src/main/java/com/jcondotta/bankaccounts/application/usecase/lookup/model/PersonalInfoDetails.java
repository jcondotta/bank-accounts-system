package com.jcondotta.bankaccounts.application.usecase.lookup.model;

import java.time.LocalDate;

public record PersonalInfoDetails(
  String firstName,
  String lastName,
  IdentityDocumentDetails identityDocument,
  LocalDate dateOfBirth
) {}