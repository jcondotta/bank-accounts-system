package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountHolderRestoreTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;
  private static final Instant CREATED_AT = Instant.now(ClockTestFactory.FIXED_CLOCK);

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldRestoreAccountHolderWithAllAttributes(AccountHolderType accountHolderType) {
    var accountHolderId = AccountHolderId.newId();

    var accountHolder = AccountHolder.restore(
      accountHolderId,
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      PRIMARY_ACCOUNT_HOLDER.address(),
      accountHolderType,
      CREATED_AT
    );

    assertThat(accountHolder.id()).isEqualTo(accountHolderId);
    assertThat(accountHolder.personalInfo()).isEqualTo(PRIMARY_ACCOUNT_HOLDER.personalInfo());
    assertThat(accountHolder.contactInfo()).isEqualTo(PRIMARY_ACCOUNT_HOLDER.contactInfo());
    assertThat(accountHolder.address()).isEqualTo(PRIMARY_ACCOUNT_HOLDER.address());
    assertThat(accountHolder.accountHolderType()).isEqualTo(accountHolderType);
    assertThat(accountHolder.createdAt()).isEqualTo(CREATED_AT);
    assertThat(accountHolder.isPrimary()).isEqualTo(accountHolderType.isPrimary());
    assertThat(accountHolder.isJoint()).isEqualTo(accountHolderType.isJoint());
  }

  @Test
  void shouldThrowException_whenIdIsNull() {
    assertThatThrownBy(() -> AccountHolder.restore(
      null,
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      PRIMARY_ACCOUNT_HOLDER.address(),
      AccountHolderType.PRIMARY,
      CREATED_AT
    ))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolder.ID_MUST_BE_PROVIDED);
  }

  @Test
  void shouldThrowException_whenPersonalInfoIsNull() {
    assertThatThrownBy(() -> AccountHolder.restore(
      AccountHolderId.newId(),
      null,
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      PRIMARY_ACCOUNT_HOLDER.address(),
      AccountHolderType.PRIMARY,
      CREATED_AT
    ))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolder.PERSONAL_INFO_MUST_BE_PROVIDED);
  }

  @Test
  void shouldThrowException_whenContactInfoIsNull() {
    assertThatThrownBy(() -> AccountHolder.restore(
      AccountHolderId.newId(),
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      null,
      PRIMARY_ACCOUNT_HOLDER.address(),
      AccountHolderType.PRIMARY,
      CREATED_AT
    ))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolder.CONTACT_INFO_MUST_BE_PROVIDED);
  }

  @Test
  void shouldThrowException_whenAddressIsNull() {
    assertThatThrownBy(() -> AccountHolder.restore(
      AccountHolderId.newId(),
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      null,
      AccountHolderType.PRIMARY,
      CREATED_AT
    ))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolder.ADDRESS_MUST_BE_PROVIDED);
  }

  @Test
  void shouldThrowException_whenAccountHolderTypeIsNull() {
    assertThatThrownBy(() -> AccountHolder.restore(
      AccountHolderId.newId(),
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      PRIMARY_ACCOUNT_HOLDER.address(),
      null,
      CREATED_AT
    ))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolder.ACCOUNT_HOLDER_TYPE_MUST_BE_PROVIDED);
  }

  @Test
  void shouldThrowException_whenCreatedAtIsNull() {
    assertThatThrownBy(() -> AccountHolder.restore(
      AccountHolderId.newId(),
      PRIMARY_ACCOUNT_HOLDER.personalInfo(),
      PRIMARY_ACCOUNT_HOLDER.contactInfo(),
      PRIMARY_ACCOUNT_HOLDER.address(),
      AccountHolderType.PRIMARY,
      null
    ))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolder.CREATED_AT_MUST_BE_PROVIDED);
  }
}