package com.jcondotta.bankaccounts.domain.fixtures;

import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;

import java.time.Clock;
import java.time.ZonedDateTime;

public final class BankAccountTestFixture {

  public static final Iban VALID_IBAN = Iban.of("ES3801283316232166447417");

  public static final AccountHolderFixtures DEFAULT_HOLDER = AccountHolderFixtures.JEFFERSON;

  public static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.CHECKING;

  public static final Currency DEFAULT_CURRENCY = Currency.EUR;

  public static final Clock DEFAULT_CLOCK = ClockTestFactory.FIXED_CLOCK;

  private BankAccountTestFixture() {
  }

  public static BankAccount openPendingAccount() {
    return openPendingAccount(DEFAULT_HOLDER, DEFAULT_ACCOUNT_TYPE, DEFAULT_CURRENCY, DEFAULT_CLOCK);
  }

  public static BankAccount openPendingAccount(AccountHolderFixtures holder) {
    return openPendingAccount(holder, DEFAULT_ACCOUNT_TYPE, DEFAULT_CURRENCY, DEFAULT_CLOCK);
  }

  public static BankAccount openPendingAccount(AccountHolderFixtures holder, AccountType accountType, Currency currency, Clock clock) {
    return BankAccount.open(
      holder.getAccountHolderName(),
      holder.getPassportNumber(),
      holder.getDateOfBirth(),
      holder.getEmail(),
      accountType,
      currency,
      VALID_IBAN,
      ZonedDateTime.now(clock)
    );
  }

  public static BankAccount openActiveAccount() {
    return openActiveAccount(DEFAULT_HOLDER, DEFAULT_ACCOUNT_TYPE, DEFAULT_CURRENCY, DEFAULT_CLOCK);
  }

  public static BankAccount openActiveAccount(AccountHolderFixtures holder) {
    return openActiveAccount(holder, DEFAULT_ACCOUNT_TYPE, DEFAULT_CURRENCY, DEFAULT_CLOCK);
  }

  public static BankAccount openActiveAccount(AccountHolderFixtures holder, AccountType accountType, Currency currency, Clock clock) {
    var account = openPendingAccount(holder, accountType, currency, clock);
    account.activate(ZonedDateTime.now(clock));
    account.pullDomainEvents();

    return account;
  }
}