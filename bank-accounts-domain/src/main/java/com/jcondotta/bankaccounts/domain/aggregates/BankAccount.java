package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.*;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotActiveException;
import com.jcondotta.bankaccounts.domain.exceptions.InvalidBankAccountStateTransitionException;
import com.jcondotta.bankaccounts.domain.validation.BankAccountValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.DomainValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import com.jcondotta.bankaccounts.domain.value_objects.address.Address;
import com.jcondotta.bankaccounts.domain.value_objects.contact.ContactInfo;
import com.jcondotta.bankaccounts.domain.value_objects.personal.PersonalInfo;
import com.jcondotta.domain.identity.EventId;
import com.jcondotta.domain.core.AggregateRoot;

import java.time.Instant;
import java.util.List;

import static com.jcondotta.domain.support.DomainPreconditions.required;

public final class BankAccount extends AggregateRoot<BankAccountId> {

  public static final AccountStatus ACCOUNT_STATUS_ON_OPENING = AccountStatus.PENDING;

  private final AccountType accountType;
  private final Currency currency;
  private final Iban iban;
  private final Instant createdAt;
  private final AccountHolders accountHolders;

  private AccountStatus accountStatus;

  private BankAccount(
    BankAccountId id,
    AccountType accountType,
    Currency currency,
    Iban iban,
    AccountStatus accountStatus,
    Instant createdAt,
    AccountHolders accountHolders
  ) {
    super(required(id, BankAccountValidationErrors.ID_NOT_NULL));
    this.accountType = required(accountType, BankAccountValidationErrors.ACCOUNT_TYPE_NOT_NULL);
    this.currency = required(currency, BankAccountValidationErrors.CURRENCY_NOT_NULL);
    this.iban = required(iban, BankAccountValidationErrors.IBAN_NOT_NULL);
    this.accountStatus = required(accountStatus, BankAccountValidationErrors.ACCOUNT_STATUS_NOT_NULL);
    this.createdAt = required(createdAt, DomainValidationErrors.CREATED_AT_NOT_NULL);
    this.accountHolders = required(accountHolders, BankAccountValidationErrors.ACCOUNT_HOLDERS_NOT_NULL);
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
      AccountHolders.of(primaryHolder)
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
    AccountHolders accountHolders
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

    Instant now = Instant.now();
    var accountHolder = AccountHolder.createJoint(personalInfo, contactInfo, address, now);
    accountHolders.add(accountHolder);

    this.registerEvent(new BankAccountJointHolderAddedEvent(EventId.newId(), this.getId(), accountHolder.getId(), now));
  }

  public void deactivateHolder(AccountHolderId accountHolderId) {
    accountHolders.deactivate(accountHolderId);
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

  public AccountHolder getPrimaryHolder() {
    return accountHolders.primary();
  }

  public List<AccountHolder> getJointHolders() {
    return accountHolders.joint();
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

  public List<AccountHolder> getActiveHolders() {
    return accountHolders.active();
  }
}