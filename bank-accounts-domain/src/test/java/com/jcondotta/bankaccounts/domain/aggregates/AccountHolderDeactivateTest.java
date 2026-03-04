package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.fixtures.BankAccountTestFixture;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class AccountHolderDeactivateTest {

  private static final Instant CREATED_AT = Instant.now(ClockTestFactory.FIXED_CLOCK);

  @Test
  void shouldDeactivateAccountHolder_whenAccountHolderIsActive() {
    var accountHolder = BankAccountTestFixture.createJointHolder(AccountHolderFixtures.JEFFERSON, CREATED_AT);
    accountHolder.deactivate();

    assertThat(accountHolder.isActive()).isFalse();
    assertThat(accountHolder.getDeactivatedAt()).isNotNull();
  }

  @Test
  void shouldKeepAccountHolderDeactivated_whenDeactivateIsCalledTwice() {
    var accountHolder = BankAccountTestFixture.createJointHolder(AccountHolderFixtures.JEFFERSON, CREATED_AT);
    accountHolder.deactivate();
    accountHolder.deactivate();

    assertThat(accountHolder.isActive()).isFalse();
    assertThat(accountHolder.getDeactivatedAt()).isNotNull();
  }
}