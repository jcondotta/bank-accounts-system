package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.*;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotActiveException;
import com.jcondotta.bankaccounts.domain.exceptions.InvalidBankAccountStateTransitionException;
import com.jcondotta.bankaccounts.domain.exceptions.MaxJointAccountHoldersExceededException;
import com.jcondotta.bankaccounts.domain.validation.BankAccountValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.DomainValidationErrors;
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

  private final List<DomainEvent> domainEvents = new ArrayList<>();

  private AccountStatus accountStatus;

  private BankAccount(
    BankAccountId bankAccountId,
    AccountType accountType,
    Currency currency,
    Iban iban,
    AccountStatus accountStatus,
    ZonedDateTime createdAt,
    List<AccountHolder> accountHolders
  ) {
    this.bankAccountId = requireNonNull(bankAccountId, BankAccountValidationErrors.ID_NOT_NULL);
    this.accountType = requireNonNull(accountType, BankAccountValidationErrors.ACCOUNT_TYPE_NOT_NULL);
    this.currency = requireNonNull(currency, BankAccountValidationErrors.CURRENCY_NOT_NULL);
    this.iban = requireNonNull(iban, BankAccountValidationErrors.IBAN_NOT_NULL);
    this.accountStatus = requireNonNull(accountStatus, BankAccountValidationErrors.ACCOUNT_STATUS_NOT_NULL);
    this.createdAt = requireNonNull(createdAt, DomainValidationErrors.CREATED_AT_NOT_NULL);
    this.accountHolders = new ArrayList<>(requireNonNull(accountHolders, BankAccountValidationErrors.ACCOUNT_HOLDERS_NOT_NULL));
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

    var bankAccount = new BankAccount(
      BankAccountId.newId(),
      accountType,
      currency,
      iban,
      ACCOUNT_STATUS_ON_OPENING,
      createdAt,
      List.of(primaryHolder)
    );

    bankAccount.registerEvent(
      new BankAccountOpenedEvent(
        EventId.newId(),
        bankAccount.getBankAccountId(),
        bankAccount.getAccountType(),
        bankAccount.getCurrency(),
        primaryHolder.getAccountHolderId(),
        createdAt
      )
    );

    return bankAccount;
  }

  public static BankAccount restore(
    BankAccountId bankAccountId,
    AccountType accountType,
    Currency currency,
    Iban iban,
    AccountStatus accountStatus,
    ZonedDateTime createdAt,
    List<AccountHolder> accountHolders
  ) {
    return new BankAccount(
      bankAccountId,
      accountType,
      currency,
      iban,
      accountStatus,
      createdAt,
      accountHolders
    );
  }

  public static AccountHolder restoreAccountHolder(
    AccountHolderId accountHolderId,
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    AccountHolderType accountHolderType,
    ZonedDateTime createdAt
  ) {
    return AccountHolder.restore(accountHolderId, accountHolderName, passportNumber, dateOfBirth, accountHolderType, createdAt);
  }

  public void activate() {
    if (accountStatus == AccountStatus.ACTIVE) {
      return;
    }

    if (accountStatus != AccountStatus.PENDING) {
      throw new InvalidBankAccountStateTransitionException(accountStatus, AccountStatus.ACTIVE);
    }

    this.accountStatus = AccountStatus.ACTIVE;
    registerEvent(new BankAccountActivatedEvent(EventId.newId(), this.getBankAccountId(), createdAt));
  }

  public void block() {
    if (accountStatus == AccountStatus.BLOCKED) {
      return;
    }

    if (accountStatus != AccountStatus.ACTIVE) {
      throw new InvalidBankAccountStateTransitionException(
        accountStatus,
        AccountStatus.BLOCKED
      );
    }

    this.accountStatus = AccountStatus.BLOCKED;
    registerEvent(new BankAccountBlockedEvent(
      EventId.newId(),
      this.getBankAccountId(),
      createdAt)
    );
  }

  public void addJointAccountHolder(AccountHolderName name, PassportNumber passportNumber, DateOfBirth dateOfBirth, ZonedDateTime createdAt) {
    if (!accountStatus.isActive()) {
      throw new BankAccountNotActiveException(accountStatus);
    }

    var jointAccountHoldersCount = (int) accountHolders.stream()
      .filter(Predicate.not(AccountHolder::isPrimaryAccountHolder))
      .count();

    if (jointAccountHoldersCount >= MAX_JOINT_ACCOUNT_HOLDERS) {
      throw new MaxJointAccountHoldersExceededException(jointAccountHoldersCount);
    }

    var accountHolder = AccountHolder.createJoint(name, passportNumber, dateOfBirth, createdAt);
    accountHolders.add(accountHolder);

    this.registerEvent(
      new JointAccountHolderAddedEvent(
        EventId.newId(),
        this.getBankAccountId(),
        accountHolder.getAccountHolderId(),
        accountHolder.getCreatedAt()
      )
    );
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

  public void registerEvent(DomainEvent event) {
    domainEvents.add(event);
  }

  public List<DomainEvent> pullDomainEvents() {
    var events = List.copyOf(domainEvents);
    domainEvents.clear();
    return events;
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

  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public List<AccountHolder> getAccountHolders() {
    return Collections.unmodifiableList(accountHolders);
  }
}