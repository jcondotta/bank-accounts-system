package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.ZonedDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountHolderTest {

  private static final AccountHolderName VALID_ACCOUNT_HOLDER_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber VALID_PASSPORT_NUMBER = AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth VALID_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth();

  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(ClockTestFactory.FIXED_CLOCK);

  @Test
  void shouldCreatePrimaryAccountHolder_whenValuesAreValid() {
    var accountHolder = AccountHolder.createPrimary(
      VALID_ACCOUNT_HOLDER_NAME,
      VALID_PASSPORT_NUMBER,
      VALID_DATE_OF_BIRTH,
      CREATED_AT
    );

    assertThat(accountHolder).satisfies(holder -> {
      assertThat(holder.getAccountHolderId()).isNotNull();
      assertThat(holder.getAccountHolderName()).isEqualTo(VALID_ACCOUNT_HOLDER_NAME);
      assertThat(holder.getPassportNumber()).isEqualTo(VALID_PASSPORT_NUMBER);
      assertThat(holder.getDateOfBirth()).isEqualTo(VALID_DATE_OF_BIRTH);
      assertThat(holder.getAccountHolderType()).isEqualTo(AccountHolderType.PRIMARY);
      assertThat(holder.getCreatedAt()).isEqualTo(CREATED_AT);
      assertThat(holder.isPrimaryAccountHolder()).isTrue();
      assertThat(holder.isJointAccountHolder()).isFalse();
    });
  }

  @Test
  void shouldCreateJointAccountHolder_whenValuesAreValid() {
    var accountHolder = AccountHolder.createJoint(
      VALID_ACCOUNT_HOLDER_NAME,
      VALID_PASSPORT_NUMBER,
      VALID_DATE_OF_BIRTH,
      CREATED_AT
    );

    assertThat(accountHolder).satisfies(holder -> {
      assertThat(holder.getAccountHolderId()).isNotNull();
      assertThat(holder.getAccountHolderName()).isEqualTo(VALID_ACCOUNT_HOLDER_NAME);
      assertThat(holder.getPassportNumber()).isEqualTo(VALID_PASSPORT_NUMBER);
      assertThat(holder.getDateOfBirth()).isEqualTo(VALID_DATE_OF_BIRTH);
      assertThat(holder.getAccountHolderType()).isEqualTo(AccountHolderType.JOINT);
      assertThat(holder.getCreatedAt()).isEqualTo(CREATED_AT);
      assertThat(holder.isJointAccountHolder()).isTrue();
      assertThat(holder.isPrimaryAccountHolder()).isFalse();
    });
  }

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldThrowNullPointerException_whenAccountHolderNameIsNull(AccountHolderType accountHolderType) {
    assertThatThrownBy(() -> createAccountHolder(null, VALID_PASSPORT_NUMBER, VALID_DATE_OF_BIRTH, accountHolderType, CREATED_AT))
      .isInstanceOf(NullPointerException.class);
  }

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldThrowNullPointerException_whenPassportNumberIsNull(AccountHolderType accountHolderType) {
    assertThatThrownBy(() -> createAccountHolder(VALID_ACCOUNT_HOLDER_NAME, null, VALID_DATE_OF_BIRTH, accountHolderType, CREATED_AT))
      .isInstanceOf(NullPointerException.class);
  }

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldThrowNullPointerException_whenDateOfBirthIsNull(AccountHolderType accountHolderType) {
    assertThatThrownBy(() -> createAccountHolder(VALID_ACCOUNT_HOLDER_NAME, VALID_PASSPORT_NUMBER, null, accountHolderType, CREATED_AT))
      .isInstanceOf(NullPointerException.class);
  }

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldThrowNullPointerException_whenCreatedAtIsNull(AccountHolderType accountHolderType) {
    assertThatThrownBy(() -> createAccountHolder(VALID_ACCOUNT_HOLDER_NAME, VALID_PASSPORT_NUMBER, VALID_DATE_OF_BIRTH, accountHolderType, null))
      .isInstanceOf(NullPointerException.class);
  }

  @SuppressWarnings("all")
  private AccountHolder createAccountHolder(AccountHolderName name, PassportNumber passportNumber, DateOfBirth dateOfBirth, AccountHolderType type, ZonedDateTime createdAt) {
    Objects.requireNonNull(type, "accountHolderType must not be null");

    if (type.isPrimary()) {
      return AccountHolder.createPrimary(name, passportNumber, dateOfBirth, createdAt);
    }
    return AccountHolder.createJoint(name, passportNumber, dateOfBirth, createdAt);
  }
}
