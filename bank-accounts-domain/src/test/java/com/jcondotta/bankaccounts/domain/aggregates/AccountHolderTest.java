package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.validation.AccountHolderValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountHolderTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;
  private static final AccountHolderFixtures JOINT_ACCOUNT_HOLDER = AccountHolderFixtures.PATRIZIO;

  private static final Instant CREATED_AT = Instant.now(ClockTestFactory.FIXED_CLOCK);

  private static final AccountHolderType ACCOUNT_HOLDER_TYPE_PRIMARY = AccountHolderType.PRIMARY;

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldCreateAccountHolder_whenValuesAreValid(AccountHolderType accountHolderType) {
    var accountHolder = AccountHolder.create(
      PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
      PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
      PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
      PRIMARY_ACCOUNT_HOLDER.getEmail(),
      accountHolderType,
      CREATED_AT
    );

    assertHolderMatchesFixture(accountHolder, PRIMARY_ACCOUNT_HOLDER);
    assertThat(accountHolder.accountHolderType()).isEqualTo(accountHolderType);
    assertThat(accountHolder.isPrimary()).isEqualTo(accountHolderType.isPrimary());
    assertThat(accountHolder.isJoint()).isEqualTo(accountHolderType.isJoint());
  }

  @Test
  void shouldCreatePrimaryAccountHolder_whenValuesAreValid() {
    var accountHolder = AccountHolder.createPrimary(
      PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
      PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
      PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
      PRIMARY_ACCOUNT_HOLDER.getEmail(),
      CREATED_AT
    );

    assertHolderMatchesFixture(accountHolder, PRIMARY_ACCOUNT_HOLDER);
    assertThat(accountHolder.isPrimary()).isTrue();
    assertThat(accountHolder.isJoint()).isFalse();
  }

  @Test
  void shouldCreateJointAccountHolder_whenValuesAreValid() {
    var accountHolder = AccountHolder.createJoint(
      JOINT_ACCOUNT_HOLDER.getAccountHolderName(),
      JOINT_ACCOUNT_HOLDER.getPassportNumber(),
      JOINT_ACCOUNT_HOLDER.getDateOfBirth(),
      JOINT_ACCOUNT_HOLDER.getEmail(),
      CREATED_AT
    );

    assertHolderMatchesFixture(accountHolder, JOINT_ACCOUNT_HOLDER);
    assertThat(accountHolder.isJoint()).isTrue();
    assertThat(accountHolder.isPrimary()).isFalse();
  }

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldRestoreAccountHolderWithAllAttributes(AccountHolderType accountHolderType) {
    var accountHolderId = AccountHolderId.newId();

    var accountHolder = AccountHolder.restore(
      accountHolderId,
      PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
      PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
      PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
      PRIMARY_ACCOUNT_HOLDER.getEmail(),
      accountHolderType,
      CREATED_AT
    );

    assertHolderMatchesFixture(accountHolder, PRIMARY_ACCOUNT_HOLDER);
    assertThat(accountHolder.id()).isEqualTo(accountHolderId);
    assertThat(accountHolder.accountHolderType()).isEqualTo(accountHolderType);
    assertThat(accountHolder.isPrimary()).isEqualTo(accountHolderType.isPrimary());
    assertThat(accountHolder.isJoint()).isEqualTo(accountHolderType.isJoint());
  }

  @Test
  void shouldThrowNullPointerException_whenAccountHolderNameIsNull() {
    assertThatThrownBy(() -> AccountHolder.create(
      null,
      PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
      PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
      PRIMARY_ACCOUNT_HOLDER.getEmail(),
      ACCOUNT_HOLDER_TYPE_PRIMARY,
      CREATED_AT))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.NAME_NOT_NULL);
  }

  @Test
  void shouldThrowNullPointerException_whenPassportNumberIsNull() {
    assertThatThrownBy(() -> AccountHolder.create(
      PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
      null,
      PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
      PRIMARY_ACCOUNT_HOLDER.getEmail(),
      ACCOUNT_HOLDER_TYPE_PRIMARY,
      CREATED_AT))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.PASSPORT_NUMBER_NOT_NULL);
  }

  @Test
  void shouldThrowNullPointerException_whenDateOfBirthIsNull() {
    assertThatThrownBy(() -> AccountHolder.create(
      PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
      PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
      null,
      PRIMARY_ACCOUNT_HOLDER.getEmail(),
      ACCOUNT_HOLDER_TYPE_PRIMARY,
      CREATED_AT))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.DATE_OF_BIRTH_NOT_NULL);
  }

  @Test
  void shouldThrowNullPointerException_whenEmailIsNull() {
    assertThatThrownBy(() -> AccountHolder.create(
      PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
      PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
      PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
      null,
      ACCOUNT_HOLDER_TYPE_PRIMARY,
      CREATED_AT))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.EMAIL_NOT_NULL);
  }

  @Test
  void shouldThrowNullPointerException_whenCreatedAtIsNull() {
    assertThatThrownBy(() -> AccountHolder.create(
      PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
      PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
      PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
      PRIMARY_ACCOUNT_HOLDER.getEmail(),
      ACCOUNT_HOLDER_TYPE_PRIMARY,
      null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.CREATED_AT_NOT_NULL);
  }

  private void assertHolderMatchesFixture(AccountHolder actual, AccountHolderFixtures expected) {
    assertThat(actual.name()).isEqualTo(expected.getAccountHolderName());
    assertThat(actual.passportNumber()).isEqualTo(expected.getPassportNumber());
    assertThat(actual.dateOfBirth()).isEqualTo(expected.getDateOfBirth());
    assertThat(actual.email()).isEqualTo(expected.getEmail());
    assertThat(actual.createdAt()).isEqualTo(CREATED_AT);
  }
}