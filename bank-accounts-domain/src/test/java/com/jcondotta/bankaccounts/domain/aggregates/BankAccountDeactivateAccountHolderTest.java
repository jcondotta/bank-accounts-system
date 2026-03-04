package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.exceptions.AccountHolderNotFoundException;
import com.jcondotta.bankaccounts.domain.exceptions.CannotDeactivatePrimaryAccountHolderException;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountDeactivateAccountHolderTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;
  private static final AccountHolderFixtures JOINT_ACCOUNT_HOLDER = AccountHolderFixtures.PATRIZIO;

  private static final Instant ACCOUNT_CREATED_AT = Instant.now(ClockTestFactory.FIXED_CLOCK);

  @Test
  void shouldDeactivateJointAccountHolder_whenAccountHolderIsJoint() {
    var primary = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);
    var joint = BankAccountTestFixture.createJointHolder(JOINT_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);

    var bankAccount = BankAccount.restore(
        BankAccountId.newId(),
        AccountType.CHECKING,
        Currency.EUR,
        BankAccountTestFixture.VALID_IBAN,
        AccountStatus.ACTIVE,
        ACCOUNT_CREATED_AT,
        List.of(primary, joint)
    );

    bankAccount.deactivateAccountHolder(joint.getId());

    assertThat(joint.isActive()).isFalse();
  }

  @Test
  void shouldThrowException_whenTryingToDeactivatePrimaryAccountHolder() {
    var primary = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);

    var bankAccount = BankAccount.restore(
        BankAccountId.newId(),
        AccountType.CHECKING,
        Currency.EUR,
        BankAccountTestFixture.VALID_IBAN,
        AccountStatus.ACTIVE,
        ACCOUNT_CREATED_AT,
        List.of(primary)
    );

    assertThatThrownBy(() -> bankAccount.deactivateAccountHolder(primary.getId()))
      .isInstanceOf(CannotDeactivatePrimaryAccountHolderException.class)
      .hasMessage(CannotDeactivatePrimaryAccountHolderException.PRIMARY_ACCOUNT_HOLDER_CANNOT_BE_DEACTIVATED);
  }

  @Test
  void shouldThrowException_whenAccountHolderDoesNotExist() {
    var primary = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);

    var bankAccount = BankAccount.restore(
        BankAccountId.newId(),
        AccountType.CHECKING,
        Currency.EUR,
        BankAccountTestFixture.VALID_IBAN,
        AccountStatus.ACTIVE,
        ACCOUNT_CREATED_AT,
        List.of(primary)
    );

    assertThatThrownBy(() -> bankAccount.deactivateAccountHolder(AccountHolderId.newId()))
      .isInstanceOf(AccountHolderNotFoundException.class);
  }
}