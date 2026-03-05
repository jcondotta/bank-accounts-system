package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.fixtures.BankAccountTestFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountHoldersJointCountTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;
  private static final AccountHolderFixtures JOINT_ACCOUNT_HOLDER = AccountHolderFixtures.PATRIZIO;

  @Test
  void shouldReturnNumberOfJointHolders_whenJointHoldersExist() {
    var primaryHolder = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER);
    var jointHolder = BankAccountTestFixture.createJointHolder(JOINT_ACCOUNT_HOLDER);

    var accountHolders = AccountHolders.of(primaryHolder, jointHolder);

    assertThat(accountHolders.jointCount()).isEqualTo(1);
  }

  @Test
  void shouldReturnZero_whenNoJointHoldersExist() {
    var primaryHolder = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER);
    var accountHolders = AccountHolders.of(primaryHolder);

    assertThat(accountHolders.jointCount()).isZero();
  }

  @Test
  void shouldIgnoreInactiveJointHolders_whenCountingJointHolders() {
    var primaryHolder = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER);
    var jointHolder = BankAccountTestFixture.createJointHolder(JOINT_ACCOUNT_HOLDER);

    jointHolder.deactivate();

    var accountHolders = AccountHolders.of(primaryHolder, jointHolder);
    assertThat(accountHolders.jointCount()).isZero();
  }
}