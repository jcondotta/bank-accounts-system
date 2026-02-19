package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.validation.AccountHolderValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountHolderTest {

  private static final AccountHolderName VALID_ACCOUNT_HOLDER_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber VALID_PASSPORT_NUMBER = AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth VALID_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth();
  private static final Email VALID_EMAIL = AccountHolderFixtures.JEFFERSON.getEmail();

  private static final Instant CREATED_AT = Instant.now(ClockTestFactory.FIXED_CLOCK);

  @Test
  void shouldCreatePrimaryAccountHolder_whenValuesAreValid() {
    var accountHolder = AccountHolder.createPrimary(
      VALID_ACCOUNT_HOLDER_NAME,
      VALID_PASSPORT_NUMBER,
      VALID_DATE_OF_BIRTH,
      VALID_EMAIL,
      CREATED_AT
    );

    assertThat(accountHolder).satisfies(holder -> {
      assertThat(holder.id()).isNotNull();
      assertThat(holder.name()).isEqualTo(VALID_ACCOUNT_HOLDER_NAME);
      assertThat(holder.passportNumber()).isEqualTo(VALID_PASSPORT_NUMBER);
      assertThat(holder.dateOfBirth()).isEqualTo(VALID_DATE_OF_BIRTH);
      assertThat(holder.email()).isEqualTo(VALID_EMAIL);
      assertThat(holder.accountHolderType()).isEqualTo(AccountHolderType.PRIMARY);
      assertThat(holder.createdAt()).isEqualTo(CREATED_AT);
      assertThat(holder.isPrimary()).isTrue();
      assertThat(holder.isJoint()).isFalse();
    });
  }

  @Test
  void shouldCreateJointAccountHolder_whenValuesAreValid() {
    var accountHolder = AccountHolder.createJoint(
      VALID_ACCOUNT_HOLDER_NAME,
      VALID_PASSPORT_NUMBER,
      VALID_DATE_OF_BIRTH,
      VALID_EMAIL,
      CREATED_AT
    );

    assertThat(accountHolder).satisfies(holder -> {
      assertThat(holder.id()).isNotNull();
      assertThat(holder.name()).isEqualTo(VALID_ACCOUNT_HOLDER_NAME);
      assertThat(holder.passportNumber()).isEqualTo(VALID_PASSPORT_NUMBER);
      assertThat(holder.dateOfBirth()).isEqualTo(VALID_DATE_OF_BIRTH);
      assertThat(holder.email()).isEqualTo(VALID_EMAIL);
      assertThat(holder.accountHolderType()).isEqualTo(AccountHolderType.JOINT);
      assertThat(holder.createdAt()).isEqualTo(CREATED_AT);
      assertThat(holder.isJoint()).isTrue();
      assertThat(holder.isPrimary()).isFalse();
    });
  }

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldRestoreAccountHolderWithAllAttributes(AccountHolderType accountHolderType) {
    var accountHolderId = AccountHolderId.newId();

    var accountHolder = AccountHolder.restore(
      accountHolderId,
      VALID_ACCOUNT_HOLDER_NAME,
      VALID_PASSPORT_NUMBER,
      VALID_DATE_OF_BIRTH,
      VALID_EMAIL,
      accountHolderType,
      CREATED_AT
    );

    assertThat(accountHolder.id()).isEqualTo(accountHolderId);
    assertThat(accountHolder.name()).isEqualTo(VALID_ACCOUNT_HOLDER_NAME);
    assertThat(accountHolder.passportNumber()).isEqualTo(VALID_PASSPORT_NUMBER);
    assertThat(accountHolder.dateOfBirth()).isEqualTo(VALID_DATE_OF_BIRTH);
    assertThat(accountHolder.email()).isEqualTo(VALID_EMAIL);
    assertThat(accountHolder.accountHolderType()).isEqualTo(accountHolderType);
    assertThat(accountHolder.createdAt()).isEqualTo(CREATED_AT);
  }

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldThrowNullPointerException_whenAccountHolderNameIsNull(AccountHolderType accountHolderType) {
    assertThatThrownBy(() -> createAccountHolder(null, VALID_PASSPORT_NUMBER, VALID_DATE_OF_BIRTH, VALID_EMAIL, accountHolderType, CREATED_AT))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.NAME_NOT_NULL);
  }

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldThrowNullPointerException_whenPassportNumberIsNull(AccountHolderType accountHolderType) {
    assertThatThrownBy(() -> createAccountHolder(VALID_ACCOUNT_HOLDER_NAME, null, VALID_DATE_OF_BIRTH, VALID_EMAIL, accountHolderType, CREATED_AT))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.PASSPORT_NUMBER_NOT_NULL);
  }

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldThrowNullPointerException_whenDateOfBirthIsNull(AccountHolderType accountHolderType) {
    assertThatThrownBy(() -> createAccountHolder(VALID_ACCOUNT_HOLDER_NAME, VALID_PASSPORT_NUMBER, null, VALID_EMAIL, accountHolderType, CREATED_AT))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.DATE_OF_BIRTH_NOT_NULL);
  }

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldThrowNullPointerException_whenEmailIsNull(AccountHolderType accountHolderType) {
    assertThatThrownBy(() -> createAccountHolder(VALID_ACCOUNT_HOLDER_NAME, VALID_PASSPORT_NUMBER, VALID_DATE_OF_BIRTH, null, accountHolderType, CREATED_AT))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.EMAIL_NOT_NULL);
  }

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldThrowNullPointerException_whenCreatedAtIsNull(AccountHolderType accountHolderType) {
    assertThatThrownBy(() -> createAccountHolder(VALID_ACCOUNT_HOLDER_NAME, VALID_PASSPORT_NUMBER, VALID_DATE_OF_BIRTH, VALID_EMAIL, accountHolderType, null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.CREATED_AT_NOT_NULL);
  }

  @SuppressWarnings("all")
  private AccountHolder createAccountHolder(AccountHolderName name, PassportNumber passportNumber, DateOfBirth dateOfBirth, Email email, AccountHolderType type, Instant createdAt) {
    Objects.requireNonNull(type, AccountHolderValidationErrors.ACCOUNT_HOLDER_TYPE);

    if (type.isPrimary()) {
      return AccountHolder.createPrimary(name, passportNumber, dateOfBirth, email, createdAt);
    }
    return AccountHolder.createJoint(name, passportNumber, dateOfBirth, email, createdAt);
  }
}
