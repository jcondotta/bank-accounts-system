package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotActiveException;
import com.jcondotta.bankaccounts.domain.exceptions.InvalidBankAccountStateTransitionException;
import com.jcondotta.bankaccounts.domain.exceptions.MaxJointAccountHoldersExceededException;
import com.jcondotta.bankaccounts.domain.value_objects.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public final class BankAccount {

  public static final AccountStatus ACCOUNT_STATUS_ON_OPENING = AccountStatus.PENDING;
  public static final int MAX_JOINT_ACCOUNT_HOLDERS = 1;

  private final BankAccountId bankAccountId;
  private final AccountType accountType;
  private final Currency currency;
  private final Iban iban;
  private final ZonedDateTime createdAt;
  private final List<AccountHolder> accountHolders;

  private AccountStatus status;

  private BankAccount(
    BankAccountId bankAccountId,
    AccountType accountType,
    Currency currency,
    Iban iban,
    AccountStatus status,
    ZonedDateTime createdAt,
    List<AccountHolder> accountHolders
  ) {
    this.bankAccountId = requireNonNull(bankAccountId, "bankAccountId must not be null");
    this.accountType = requireNonNull(accountType, "accountType must not be null");
    this.currency = requireNonNull(currency, "currency must not be null");
    this.iban = requireNonNull(iban, "iban must not be null");
    this.status = requireNonNull(status, "status must not be null");
    this.createdAt = requireNonNull(createdAt, "createdAt must not be null");
    this.accountHolders = new ArrayList<>(requireNonNull(accountHolders, "accountHolders must not be null"));
  }

  public static BankAccount open(
    AccountHolderName name,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    AccountType accountType,
    Currency currency,
    Iban iban,
    ZonedDateTime createdAt
  ) {
    var primaryHolder = AccountHolder.createPrimary(name, passportNumber, dateOfBirth, createdAt);

    return new BankAccount(
      BankAccountId.newId(),
      accountType,
      currency,
      iban,
      ACCOUNT_STATUS_ON_OPENING,
      createdAt,
      List.of(primaryHolder)
    );
  }

  public void activate() {
    if (status == AccountStatus.ACTIVE) {
      return;
    }

    if (status != AccountStatus.PENDING) {
      throw new InvalidBankAccountStateTransitionException(status, AccountStatus.ACTIVE);
    }

    this.status = AccountStatus.ACTIVE;
  }

  public void block() {
    if (status == AccountStatus.BLOCKED) {
      return;
    }

    if (status != AccountStatus.ACTIVE) {
      throw new InvalidBankAccountStateTransitionException(
        status,
        AccountStatus.BLOCKED
      );
    }

    this.status = AccountStatus.BLOCKED;
  }

  public void addJointAccountHolder(AccountHolderName name, PassportNumber passportNumber, DateOfBirth dateOfBirth, ZonedDateTime createdAt) {
    if (!status.isActive()) {
      throw new BankAccountNotActiveException(status);
    }

    var jointAccountHoldersCount = (int) accountHolders.stream()
      .filter(Predicate.not(AccountHolder::isPrimaryAccountHolder))
      .count();

    if (jointAccountHoldersCount >= MAX_JOINT_ACCOUNT_HOLDERS) {
      throw new MaxJointAccountHoldersExceededException(jointAccountHoldersCount);
    }

    var accountHolder = AccountHolder.createJoint(name, passportNumber, dateOfBirth, createdAt);
    accountHolders.add(accountHolder);
  }

  public Optional<AccountHolder> findAccountHolder(AccountHolderId accountHolderId) {
    return accountHolders.stream()
      .filter(accountHolder -> accountHolder.getAccountHolderId().equals(accountHolderId))
      .findFirst();
  }

  public AccountHolder primaryAccountHolder() {
    return accountHolders.stream()
      .filter(AccountHolder::isPrimaryAccountHolder)
      .findFirst()
      .orElseThrow(IllegalStateException::new);
  }

  public List<AccountHolder> jointAccountHolders() {
    return accountHolders.stream()
      .filter(AccountHolder::isJointAccountHolder)
      .toList();
  }

  public BankAccountId getBankAccountId() {
    return bankAccountId;
  }

  public AccountType getAccountType() {
    return accountType;
  }

  public Currency getCurrency() {
    return currency;
  }

  public Iban getIban() {
    return iban;
  }

  public AccountStatus getStatus() {
    return status;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public List<AccountHolder> getAccountHolders() {
    return Collections.unmodifiableList(accountHolders);
  }
}