package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountHolderCreateTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;
  private static final AccountHolderFixtures JOINT_ACCOUNT_HOLDER = AccountHolderFixtures.PATRIZIO;

  private static final Instant CREATED_AT = Instant.now(ClockTestFactory.FIXED_CLOCK);

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldCreateAccountHolder_whenValuesAreValid(AccountHolderType accountHolderType) {
    var accountHolder = AccountHolder.create(
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      PRIMARY_ACCOUNT_HOLDER.address(),
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
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      PRIMARY_ACCOUNT_HOLDER.address(),
      CREATED_AT
    );

    assertHolderMatchesFixture(accountHolder, PRIMARY_ACCOUNT_HOLDER);
    assertThat(accountHolder.isPrimary()).isTrue();
    assertThat(accountHolder.isJoint()).isFalse();
  }

  @Test
  void shouldCreateJointAccountHolder_whenValuesAreValid() {
    var accountHolder = AccountHolder.createJoint(
      JOINT_ACCOUNT_HOLDER.personalInfo(),
      JOINT_ACCOUNT_HOLDER.contactInfo(),
      JOINT_ACCOUNT_HOLDER.address(),
      CREATED_AT
    );

    assertHolderMatchesFixture(accountHolder, JOINT_ACCOUNT_HOLDER);
    assertThat(accountHolder.isJoint()).isTrue();
    assertThat(accountHolder.isPrimary()).isFalse();
  }

  @Test
  void shouldThrowException_whenPersonalInfoIsNull() {
    assertThatThrownBy(() -> AccountHolder.create(
      null,
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      PRIMARY_ACCOUNT_HOLDER.address(),
      AccountHolderType.PRIMARY,
      CREATED_AT))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolder.PERSONAL_INFO_MUST_BE_PROVIDED);
  }

  @Test
  void shouldThrowException_whenContactInfoIsNull() {
    assertThatThrownBy(() -> AccountHolder.create(
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      null,
      PRIMARY_ACCOUNT_HOLDER.address(),
      AccountHolderType.PRIMARY,
      CREATED_AT))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolder.CONTACT_INFO_MUST_BE_PROVIDED);
  }

  @Test
  void shouldThrowException_whenAddressIsNull() {
    assertThatThrownBy(() -> AccountHolder.create(
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      null,
      AccountHolderType.PRIMARY,
      CREATED_AT))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolder.ADDRESS_MUST_BE_PROVIDED);
  }

  @Test
  void shouldThrowException_whenAccountHolderTypeIsNull() {
    assertThatThrownBy(() -> AccountHolder.create(
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      PRIMARY_ACCOUNT_HOLDER.address(),
      null,
      CREATED_AT))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolder.ACCOUNT_HOLDER_TYPE_MUST_BE_PROVIDED);
  }

  @Test
  void shouldThrowException_whenCreatedAtIsNull() {
    assertThatThrownBy(() -> AccountHolder.create(
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      PRIMARY_ACCOUNT_HOLDER.address(),
      AccountHolderType.PRIMARY,
      null))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolder.CREATED_AT_MUST_BE_PROVIDED);
  }

  private void assertHolderMatchesFixture(AccountHolder actual, AccountHolderFixtures expected) {
    assertThat(actual.personalInfo()).isEqualTo(expected.personalInfo());
    assertThat(actual.contactInfo()).isEqualTo(expected.contactInfo());
    assertThat(actual.address()).isEqualTo(expected.address());
    assertThat(actual.createdAt()).isEqualTo(CREATED_AT);
  }
}