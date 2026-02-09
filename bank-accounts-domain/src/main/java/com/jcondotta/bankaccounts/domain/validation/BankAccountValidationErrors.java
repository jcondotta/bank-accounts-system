package com.jcondotta.bankaccounts.domain.validation;

public interface BankAccountValidationErrors extends DomainValidationErrors {

  String ID_NOT_NULL = "bankAccountId must not be null";
  String ACCOUNT_TYPE_NOT_NULL = "accountType must not be null";
  String CURRENCY_NOT_NULL = "currency must not be null";
  String ACCOUNT_STATUS_NOT_NULL = "account status must not be null";
  String IBAN_NOT_NULL = "iban must not be null";
  String ACCOUNT_HOLDERS_NOT_NULL = "accountHolders must not be null";
}