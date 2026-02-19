package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.validation.AccountHolderValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.*;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public final class AccountHolder {

  private final AccountHolderId id;
  private final AccountHolderName name;
  private final PassportNumber passportNumber;
  private final DateOfBirth dateOfBirth;
  private final Email email;
  private final AccountHolderType accountHolderType;
  private final Instant createdAt;

  private AccountHolder(
    AccountHolderId id,
    AccountHolderName name,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    Email email,
    AccountHolderType accountHolderType,
    Instant createdAt
  ) {
    this.id = requireNonNull(id, AccountHolderValidationErrors.ID_NOT_NULL);
    this.name = requireNonNull(name, AccountHolderValidationErrors.NAME_NOT_NULL);
    this.passportNumber = requireNonNull(passportNumber, AccountHolderValidationErrors.PASSPORT_NUMBER_NOT_NULL);
    this.dateOfBirth = requireNonNull(dateOfBirth, AccountHolderValidationErrors.DATE_OF_BIRTH_NOT_NULL);
    this.email = requireNonNull(email, AccountHolderValidationErrors.EMAIL_NOT_NULL);
    this.accountHolderType = requireNonNull(accountHolderType, AccountHolderValidationErrors.ACCOUNT_HOLDER_TYPE);
    this.createdAt = requireNonNull(createdAt, AccountHolderValidationErrors.CREATED_AT_NOT_NULL);
  }

  static AccountHolder createPrimary(AccountHolderName accountHolderName, PassportNumber passportNumber, DateOfBirth dateOfBirth, Email email, Instant createdAt) {
    return create(accountHolderName, passportNumber, dateOfBirth, email, AccountHolderType.PRIMARY, createdAt);
  }

  static AccountHolder createJoint(AccountHolderName accountHolderName, PassportNumber passportNumber, DateOfBirth dateOfBirth, Email email, Instant createdAt) {
    return create(accountHolderName, passportNumber, dateOfBirth, email, AccountHolderType.JOINT, createdAt);
  }

  private static AccountHolder create(
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    Email email,
    AccountHolderType accountHolderType,
    Instant createdAt) {

    return new AccountHolder(AccountHolderId.newId(), accountHolderName, passportNumber, dateOfBirth, email, accountHolderType, createdAt);
  }

  static AccountHolder restore(
    AccountHolderId accountHolderId,
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    Email email,
    AccountHolderType accountHolderType,
    Instant createdAt) {

    return new AccountHolder(accountHolderId, accountHolderName, passportNumber, dateOfBirth, email, accountHolderType, createdAt);
  }

  public boolean isPrimary() {
    return accountHolderType.isPrimary();
  }

  public boolean isJoint() {
    return accountHolderType.isJoint();
  }

  public AccountHolderId id() {
    return id;
  }

  public AccountHolderName name() {
    return name;
  }

  public PassportNumber passportNumber() {
    return passportNumber;
  }

  public DateOfBirth dateOfBirth() {
    return dateOfBirth;
  }

  public Email email() {
    return email;
  }

  public AccountHolderType accountHolderType() {
    return accountHolderType;
  }

  public Instant createdAt() {
    return createdAt;
  }
}
