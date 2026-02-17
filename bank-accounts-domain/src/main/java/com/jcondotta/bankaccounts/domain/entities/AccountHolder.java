package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.validation.AccountHolderValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.*;

import java.time.ZonedDateTime;

import static java.util.Objects.requireNonNull;

public final class AccountHolder {

  private final AccountHolderId accountHolderId;
  private final AccountHolderName accountHolderName;
  private final PassportNumber passportNumber;
  private final DateOfBirth dateOfBirth;
  private final Email email;
  private final AccountHolderType accountHolderType;
  private final ZonedDateTime createdAt;

  private AccountHolder(
    AccountHolderId accountHolderId,
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    Email email,
    AccountHolderType accountHolderType,
    ZonedDateTime createdAt
  ) {
    this.accountHolderId = requireNonNull(accountHolderId, AccountHolderValidationErrors.ID_NOT_NULL);
    this.accountHolderName = requireNonNull(accountHolderName, AccountHolderValidationErrors.NAME_NOT_NULL);
    this.passportNumber = requireNonNull(passportNumber, AccountHolderValidationErrors.PASSPORT_NUMBER_NOT_NULL);
    this.dateOfBirth = requireNonNull(dateOfBirth, AccountHolderValidationErrors.DATE_OF_BIRTH_NOT_NULL);
    this.email = requireNonNull(email, AccountHolderValidationErrors.EMAIL_NOT_NULL);
    this.accountHolderType = requireNonNull(accountHolderType, AccountHolderValidationErrors.ACCOUNT_HOLDER_TYPE);
    this.createdAt = requireNonNull(createdAt, AccountHolderValidationErrors.CREATED_AT_NOT_NULL);
  }

  static AccountHolder createPrimary(AccountHolderName accountHolderName, PassportNumber passportNumber, DateOfBirth dateOfBirth, Email email, ZonedDateTime createdAt) {
    return create(accountHolderName, passportNumber, dateOfBirth, email, AccountHolderType.PRIMARY, createdAt);
  }

  static AccountHolder createJoint(AccountHolderName accountHolderName, PassportNumber passportNumber, DateOfBirth dateOfBirth, Email email, ZonedDateTime createdAt) {
    return create(accountHolderName, passportNumber, dateOfBirth, email, AccountHolderType.JOINT, createdAt);
  }

  private static AccountHolder create(
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    Email email,
    AccountHolderType accountHolderType,
    ZonedDateTime createdAt) {

    return new AccountHolder(AccountHolderId.newId(), accountHolderName, passportNumber, dateOfBirth, email, accountHolderType, createdAt);
  }

  static AccountHolder restore(
    AccountHolderId accountHolderId,
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    Email email,
    AccountHolderType accountHolderType,
    ZonedDateTime createdAt) {

    return new AccountHolder(accountHolderId, accountHolderName, passportNumber, dateOfBirth, email, accountHolderType, createdAt);
  }

  public boolean isPrimaryAccountHolder() {
    return accountHolderType.isPrimary();
  }

  public boolean isJointAccountHolder() {
    return accountHolderType.isJoint();
  }

  public AccountHolderId getAccountHolderId() {
    return accountHolderId;
  }

  public AccountHolderName getAccountHolderName() {
    return accountHolderName;
  }

  public PassportNumber getPassportNumber() {
    return passportNumber;
  }

  public DateOfBirth getDateOfBirth() {
    return dateOfBirth;
  }

  public Email getEmail() {
    return email;
  }

  public AccountHolderType getAccountHolderType() {
    return accountHolderType;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }
}
