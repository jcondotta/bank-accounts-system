package com.jcondotta.bankaccounts.application.fixtures;

import com.jcondotta.bankaccounts.domain.aggregates.AccountHolder;
import com.jcondotta.bankaccounts.domain.aggregates.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;

import java.time.Instant;

public final class BankAccountTestFixture {

  public static final Iban VALID_IBAN = Iban.of("ES3801283316232166447417");

  public static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.CHECKING;

  public static final Currency DEFAULT_CURRENCY = Currency.EUR;

  private BankAccountTestFixture() {
  }

  public static BankAccount openPendingAccount(AccountHolderFixtures fixtures) {
    return openPendingAccount(fixtures, DEFAULT_ACCOUNT_TYPE, DEFAULT_CURRENCY);
  }

  public static BankAccount openPendingAccount(AccountHolderFixtures fixtures, AccountType accountType, Currency currency) {
    return BankAccount.open(
      fixtures.personalInfo(),
      fixtures.contactInfo(),
      fixtures.address(),
      accountType,
      currency,
      VALID_IBAN
    );
  }

  public static BankAccount openActiveAccount(AccountHolderFixtures fixtures) {
    return openActiveAccount(fixtures, DEFAULT_ACCOUNT_TYPE, DEFAULT_CURRENCY);
  }

  public static BankAccount openActiveAccount(AccountHolderFixtures fixtures, AccountType accountType, Currency currency) {
    var account = openPendingAccount(fixtures, accountType, currency);
    account.activate();
    account.pullEvents();

    return account;
  }

  public static AccountHolder createPrimaryHolder(AccountHolderFixtures fixtures, Instant createdAt) {
    return BankAccount.restoreAccountHolder(
      AccountHolderId.newId(),
      fixtures.personalInfo(),
      fixtures.contactInfo(),
      fixtures.address(),
      AccountHolderType.PRIMARY,
      createdAt
    );
  }

  public static AccountHolder createJointHolder(AccountHolderFixtures fixtures, Instant createdAt) {
    return BankAccount.restoreAccountHolder(
      AccountHolderId.newId(),
      fixtures.personalInfo(),
      fixtures.contactInfo(),
      fixtures.address(),
      AccountHolderType.JOINT,
      createdAt
    );
  }
}