package com.jcondotta.bankaccounts.domain.fixtures;

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

  public static BankAccount openPendingAccount(AccountHolderFixtures holder) {
    return openPendingAccount(holder, DEFAULT_ACCOUNT_TYPE, DEFAULT_CURRENCY);
  }

  public static BankAccount openPendingAccount(AccountHolderFixtures holder, AccountType accountType, Currency currency) {
    return BankAccount.open(
      holder.getAccountHolderName(),
      holder.getPassportNumber(),
      holder.getDateOfBirth(),
      holder.getEmail(),
      accountType,
      currency,
      VALID_IBAN
    );
  }

  public static BankAccount openActiveAccount(AccountHolderFixtures holder) {
    return openActiveAccount(holder, DEFAULT_ACCOUNT_TYPE, DEFAULT_CURRENCY);
  }

  public static BankAccount openActiveAccount(AccountHolderFixtures holder, AccountType accountType, Currency currency) {
    var account = openPendingAccount(holder, accountType, currency);
    account.activate();
    account.pullEvents();

    return account;
  }

  public static AccountHolder createPrimaryHolder(AccountHolderFixtures fixture, Instant createdAt) {
    return BankAccount.restoreAccountHolder(
      AccountHolderId.newId(),
      fixture.getAccountHolderName(),
      fixture.getPassportNumber(),
      fixture.getDateOfBirth(),
      fixture.getEmail(),
      AccountHolderType.PRIMARY,
      createdAt
    );
  }

  public static AccountHolder createJointHolder(AccountHolderFixtures fixture, Instant createdAt) {
    return BankAccount.restoreAccountHolder(
      AccountHolderId.newId(),
      fixture.getAccountHolderName(),
      fixture.getPassportNumber(),
      fixture.getDateOfBirth(),
      fixture.getEmail(),
      AccountHolderType.JOINT,
      createdAt
    );
  }
}