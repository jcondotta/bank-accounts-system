package com.jcondotta.bankaccounts.domain.validation;

public interface AccountHolderValidationErrors extends DomainValidationErrors {

  String ID_NOT_NULL = "accountHolderId must not be null";
  String NAME_NOT_NULL = "accountHolderName must not be null";
  String PASSPORT_NUMBER_NOT_NULL = "passportNumber must not be null";
  String DATE_OF_BIRTH_NOT_NULL = "dateOfBirth must not be null";
  String ACCOUNT_HOLDER_TYPE = "accountHolderType must not be null";
  String CREATED_AT_NOT_NULL = "createdAt must not be null";
}