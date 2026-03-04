package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.*;
import com.jcondotta.bankaccounts.domain.exceptions.*;
import com.jcondotta.bankaccounts.domain.validation.BankAccountValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.DomainValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import com.jcondotta.bankaccounts.domain.value_objects.address.Address;
import com.jcondotta.bankaccounts.domain.value_objects.contact.ContactInfo;
import com.jcondotta.bankaccounts.domain.value_objects.personal.PersonalInfo;
import com.jcondotta.domain.events.EventId;
import com.jcondotta.domain.model.AggregateRoot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.jcondotta.domain.validation.DomainPreconditions.required;

public final class BankAccount extends AggregateRoot<BankAccountId> {

  public static final AccountStatus ACCOUNT_STATUS_ON_OPENING = AccountStatus.PENDING;
  public static final int MIN_PRIMARY_ACCOUNT_HOLDERS = 1;
  public static final int MAX_PRIMARY_ACCOUNT_HOLDERS = 1;
  public static final int MAX_JOINT_ACCOUNT_HOLDERS = 1;

  private final AccountType accountType;
  private final Currency currency;
  private final Iban iban;
  private final Instant createdAt;
  private final List<AccountHolder> accountHolders;

  private AccountStatus accountStatus;

  private BankAccount(
    BankAccountId id,
    AccountType accountType,
    Currency currency,
    Iban iban,
    AccountStatus accountStatus,
    Instant createdAt,
    List<AccountHolder> accountHolders
  ) {
    super(required(id, BankAccountValidationErrors.ID_NOT_NULL));
    this.accountType = required(accountType, BankAccountValidationErrors.ACCOUNT_TYPE_NOT_NULL);
    this.currency = required(currency, BankAccountValidationErrors.CURRENCY_NOT_NULL);
    this.iban = required(iban, BankAccountValidationErrors.IBAN_NOT_NULL);
    this.accountStatus = required(accountStatus, BankAccountValidationErrors.ACCOUNT_STATUS_NOT_NULL);
    this.createdAt = required(createdAt, DomainValidationErrors.CREATED_AT_NOT_NULL);

    required(accountHolders, BankAccountValidationErrors.ACCOUNT_HOLDERS_NOT_NULL);

    validateAccountHoldersConfiguration(accountHolders);
    this.accountHolders = new ArrayList<>(accountHolders);
  }

  public static BankAccount open(
    PersonalInfo personalInfo,
    ContactInfo contactInfo,
    Address address,
    AccountType accountType,
    Currency currency,
    Iban iban
  ) {
    Instant now = Instant.now();
    var primaryHolder = AccountHolder.createPrimary(personalInfo, contactInfo, address, now);

    var bankAccount = new BankAccount(
      BankAccountId.newId(),
      accountType,
      currency,
      iban,
      ACCOUNT_STATUS_ON_OPENING,
      now,
      List.of(primaryHolder)
    );

    bankAccount.registerEvent(
      new BankAccountOpenedEvent(
        EventId.newId(),
        bankAccount.getId(),
        bankAccount.getAccountType(),
        bankAccount.getCurrency(),
        primaryHolder.getId(),
        now
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
    Instant createdAt,
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
    PersonalInfo personalInfo,
    ContactInfo contactInfo,
    Address address,
    AccountHolderType accountHolderType,
    Instant createdAt
  ) {
    return AccountHolder.restore(accountHolderId, personalInfo, contactInfo, address, accountHolderType, createdAt);
  }

  public void activate() {
    if (accountStatus == AccountStatus.ACTIVE) {
      return;
    }

    if (accountStatus != AccountStatus.PENDING) {
      throw new InvalidBankAccountStateTransitionException(accountStatus, AccountStatus.ACTIVE);
    }

    this.accountStatus = AccountStatus.ACTIVE;
    registerEvent(new BankAccountActivatedEvent(EventId.newId(), this.getId(), Instant.now()));
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
    registerEvent(new BankAccountBlockedEvent(EventId.newId(), this.getId(), Instant.now()));
  }

  public void unblock() {
    if (accountStatus == AccountStatus.ACTIVE) {
      return;
    }

    if (accountStatus != AccountStatus.BLOCKED) {
      throw new InvalidBankAccountStateTransitionException(accountStatus, AccountStatus.ACTIVE);
    }

    this.accountStatus = AccountStatus.ACTIVE;
    registerEvent(new BankAccountUnblockedEvent(EventId.newId(), this.getId(), Instant.now()));
  }

  public void addJointAccountHolder(PersonalInfo personalInfo, ContactInfo contactInfo, Address address) {
    if (!accountStatus.isActive()) {
      throw new BankAccountNotActiveException(accountStatus);
    }

    int jointCount = (int) this.accountHolders.stream()
      .filter(AccountHolder::isJoint)
      .count();

    if (jointCount >= MAX_JOINT_ACCOUNT_HOLDERS) {
      throw new MaxJointAccountHoldersExceededException(jointCount);
    }

    Instant now = Instant.now();
    var accountHolder = AccountHolder.createJoint(personalInfo, contactInfo, address, now);
    accountHolders.add(accountHolder);

    this.registerEvent(new JointAccountHolderAddedEvent(EventId.newId(), this.getId(), accountHolder.getId(), now));
  }

  public void deactivateAccountHolder(AccountHolderId accountHolderId) {
    var accountHolder = findAccountHolder(accountHolderId);

    if (accountHolder.isPrimary()) {
      throw new CannotDeactivatePrimaryAccountHolderException();
    }

    accountHolder.deactivate();
  }

  public void close() {
    if (accountStatus == AccountStatus.CLOSED) {
      return;
    }

    if (accountStatus != AccountStatus.ACTIVE) {
      throw new InvalidBankAccountStateTransitionException(accountStatus, AccountStatus.CLOSED);
    }

    this.accountStatus = AccountStatus.CLOSED;

    registerEvent(new BankAccountClosedEvent(EventId.newId(), this.getId(), Instant.now()));
  }

  public AccountHolder primaryAccountHolder() {
    return accountHolders.stream()
      .filter(AccountHolder::isPrimary)
      .findFirst()
      .orElseThrow();
  }

  public List<AccountHolder> jointAccountHolders() {
    return accountHolders.stream()
      .filter(AccountHolder::isJoint)
      .toList();
  }

  private AccountHolder findAccountHolder(AccountHolderId accountHolderId) {
    return accountHolders.stream()
      .filter(holder -> holder.getId().equals(accountHolderId))
      .findFirst()
      .orElseThrow(() -> new AccountHolderNotFoundException(accountHolderId));
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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public List<AccountHolder> getAccountHolders() {
    return accountHolders.stream()
      .filter(AccountHolder::isActive)
      .sorted(Comparator.comparing(
        holder -> holder.getAccountHolderType() == AccountHolderType.PRIMARY ? 0 : 1
      ))
      .toList();
  }

  private void validateAccountHoldersConfiguration(List<AccountHolder> holders) {
    validatePrimaryAccountHolders(holders);
    validateJointAccountHolders(holders);
  }

  private void validatePrimaryAccountHolders(List<AccountHolder> holders) {
    int primaryCount = (int) holders.stream()
      .filter(AccountHolder::isPrimary)
      .count();

    if (primaryCount < MIN_PRIMARY_ACCOUNT_HOLDERS || primaryCount > MAX_PRIMARY_ACCOUNT_HOLDERS) {
      throw new InvalidBankAccountHoldersConfigurationException(
        primaryCount,
        MIN_PRIMARY_ACCOUNT_HOLDERS,
        MAX_PRIMARY_ACCOUNT_HOLDERS
      );
    }
  }

  private void validateJointAccountHolders(List<AccountHolder> holders) {
    int jointCount = (int) holders.stream()
      .filter(AccountHolder::isJoint)
      .count();

    if (jointCount > MAX_JOINT_ACCOUNT_HOLDERS) {
      throw new MaxJointAccountHoldersExceededException(jointCount);
    }
  }
}