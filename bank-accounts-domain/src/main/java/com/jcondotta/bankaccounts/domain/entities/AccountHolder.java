package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;

import java.time.ZonedDateTime;

import static java.util.Objects.requireNonNull;

public final class AccountHolder {

  private final AccountHolderId accountHolderId;
  private final AccountHolderName accountHolderName;
  private final PassportNumber passportNumber;
  private final DateOfBirth dateOfBirth;
  private final AccountHolderType accountHolderType;
  private final ZonedDateTime createdAt;

  private AccountHolder(
    AccountHolderId accountHolderId,
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    AccountHolderType accountHolderType,
    ZonedDateTime createdAt
  ) {
    this.accountHolderId = requireNonNull(accountHolderId, "accountHolderId must not be null");
    this.accountHolderName = requireNonNull(accountHolderName, "accountHolderName must not be null");
    this.passportNumber = requireNonNull(passportNumber, "passportNumber must not be null");
    this.dateOfBirth = requireNonNull(dateOfBirth, "dateOfBirth must not be null");
    this.accountHolderType = requireNonNull(accountHolderType, "accountHolderType must not be null");
    this.createdAt = requireNonNull(createdAt, "createdAt must not be null");
  }

  static AccountHolder createPrimary(AccountHolderName accountHolderName, PassportNumber passportNumber, DateOfBirth dateOfBirth, ZonedDateTime createdAt) {
    return create(accountHolderName, passportNumber, dateOfBirth, AccountHolderType.PRIMARY, createdAt);
  }

  static AccountHolder createJoint(AccountHolderName accountHolderName, PassportNumber passportNumber, DateOfBirth dateOfBirth, ZonedDateTime createdAt) {
    return create(accountHolderName, passportNumber, dateOfBirth, AccountHolderType.JOINT, createdAt);
  }

  private static AccountHolder create(AccountHolderName accountHolderName, PassportNumber passportNumber, DateOfBirth dateOfBirth, AccountHolderType accountHolderType, ZonedDateTime createdAt) {
    return new AccountHolder(AccountHolderId.newId(), accountHolderName, passportNumber, dateOfBirth, accountHolderType, createdAt);
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

  public AccountHolderType getAccountHolderType() {
    return accountHolderType;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }
}
