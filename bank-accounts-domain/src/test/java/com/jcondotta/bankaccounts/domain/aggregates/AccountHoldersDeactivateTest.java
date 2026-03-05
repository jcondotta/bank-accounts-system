package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.exceptions.AccountHolderNotFoundException;
import com.jcondotta.bankaccounts.domain.exceptions.CannotDeactivatePrimaryHolderException;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountHoldersDeactivateTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;
  private static final AccountHolderFixtures JOINT_ACCOUNT_HOLDER = AccountHolderFixtures.PATRIZIO;

  @Test
  void shouldDeactivateJointHolder_whenJointHolderExists() {
    var primaryHolder = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER);
    var jointHolder = BankAccountTestFixture.createJointHolder(JOINT_ACCOUNT_HOLDER);

    var accountHolders = AccountHolders.of(primaryHolder, jointHolder);

    accountHolders.deactivate(jointHolder.getId());

    assertThat(jointHolder.isActive()).isFalse();
  }

  @Test
  void shouldThrowException_whenHolderDoesNotExist() {
    var primaryHolder = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER);

    var accountHolders = AccountHolders.of(primaryHolder);

    assertThatThrownBy(() -> accountHolders.deactivate(AccountHolderId.newId()))
      .isInstanceOf(AccountHolderNotFoundException.class);
  }

  @Test
  void shouldThrowException_whenTryingToDeactivatePrimaryHolder() {
    var primaryHolder = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER);

    var accountHolders = AccountHolders.of(primaryHolder);

    assertThatThrownBy(() -> accountHolders.deactivate(primaryHolder.getId()))
      .isInstanceOf(CannotDeactivatePrimaryHolderException.class);
  }
}