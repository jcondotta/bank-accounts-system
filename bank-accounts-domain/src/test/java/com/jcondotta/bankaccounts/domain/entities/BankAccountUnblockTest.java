package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.BankAccountUnblockedEvent;
import com.jcondotta.bankaccounts.domain.exceptions.InvalidBankAccountStateTransitionException;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountUnblockTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;

  private static final Iban VALID_IBAN = BankAccountTestFixture.VALID_IBAN;
  private static final AccountType ACCOUNT_TYPE_SAVINGS = AccountType.SAVINGS;
  private static final Currency CURRENCY_USD = Currency.USD;

  private static final Clock ACCOUNT_CREATION_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final Instant ACCOUNT_CREATED_AT = Instant.now(ACCOUNT_CREATION_CLOCK);

  private static final Clock ACCOUNT_CHANGED_STATE_CLOCK =
    Clock.fixed(ACCOUNT_CREATED_AT.plus(2, ChronoUnit.HOURS), ZoneOffset.UTC);

  @Test
  void shouldUnblockBankAccount_whenStatusIsBlocked() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);
    bankAccount.block(ACCOUNT_CHANGED_STATE_CLOCK);
    bankAccount.pullDomainEvents();

    bankAccount.unblock(ACCOUNT_CHANGED_STATE_CLOCK);

    assertThat(bankAccount.accountStatus().isActive()).isTrue();

    var events = bankAccount.pullDomainEvents();

    assertThat(events)
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(BankAccountUnblockedEvent.class, event -> {
        assertThat(event.bankAccountId()).isEqualTo(bankAccount.id());
        assertThat(event.occurredAt()).isEqualTo(Instant.now(ACCOUNT_CHANGED_STATE_CLOCK));
      });
  }

  @Test
  void shouldNotThrowAnyException_whenUnblockIsCalledTwice() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.block(ACCOUNT_CHANGED_STATE_CLOCK);
    bankAccount.unblock(ACCOUNT_CHANGED_STATE_CLOCK);
    bankAccount.unblock(ACCOUNT_CHANGED_STATE_CLOCK);

    assertThat(bankAccount.accountStatus().isActive()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(value = AccountStatus.class, names = {"BLOCKED", "ACTIVE"}, mode = EnumSource.Mode.EXCLUDE)
  void shouldThrowInvalidBankAccountStateTransitionException_whenUnblockingFromInvalidState(AccountStatus status) {
    var primaryAccountHolder = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER, ACCOUNT_CREATION_CLOCK);

    var bankAccount = BankAccount.restore(
      BankAccountId.newId(),
      ACCOUNT_TYPE_SAVINGS,
      CURRENCY_USD,
      VALID_IBAN,
      status,
      ACCOUNT_CREATED_AT,
      List.of(primaryAccountHolder)
    );

    assertThatThrownBy(() -> bankAccount.unblock(ACCOUNT_CHANGED_STATE_CLOCK))
      .isInstanceOf(InvalidBankAccountStateTransitionException.class);
  }
}
