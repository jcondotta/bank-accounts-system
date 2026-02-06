package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.arguments_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotActiveException;
import com.jcondotta.bankaccounts.domain.exceptions.InvalidBankAccountStateTransitionException;
import com.jcondotta.bankaccounts.domain.exceptions.MaxJointAccountHoldersExceededException;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountTest {
  private static final Iban VALID_IBAN = Iban.of("ES3801283316232166447417");

  private static final AccountHolderName PRIMARY_ACCOUNT_HOLDER_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber PRIMARY_PASSPORT_NUMBER = AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth PRIMARY_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth();

  private static final AccountHolderName JOINT_ACCOUNT_HOLDER_NAME = AccountHolderFixtures.PATRIZIO.getAccountHolderName();
  private static final PassportNumber JOINT_PASSPORT_NUMBER = AccountHolderFixtures.PATRIZIO.getPassportNumber();
  private static final DateOfBirth JOINT_DATE_OF_BIRTH = AccountHolderFixtures.PATRIZIO.getDateOfBirth();

  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(ClockTestFactory.FIXED_CLOCK);

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldOpenBankAccountWithPrimaryHolder_whenValidDataProvided(AccountType accountType, Currency currency) {
    var bankAccount = openValidBankAccount(accountType, currency);

    assertThat(bankAccount)
      .satisfies(account -> {
        assertThat(account.getBankAccountId()).isNotNull();
        assertThat(account.getAccountType()).isEqualTo(accountType);
        assertThat(account.getCurrency()).isEqualTo(currency);
        assertThat(account.getIban()).isEqualTo(VALID_IBAN);
        assertThat(account.getStatus()).isEqualTo(BankAccount.ACCOUNT_STATUS_ON_OPENING);
        assertThat(account.getCreatedAt()).isEqualTo(CREATED_AT);
        assertThat(account.getAccountHolders())
          .hasSize(1)
          .first()
          .satisfies(accountHolder -> {
            assertThat(accountHolder.getAccountHolderId()).isNotNull();
            assertThat(accountHolder.getAccountHolderName()).isEqualTo(PRIMARY_ACCOUNT_HOLDER_NAME);
            assertThat(accountHolder.getPassportNumber()).isEqualTo(PRIMARY_PASSPORT_NUMBER);
            assertThat(accountHolder.getDateOfBirth()).isEqualTo(PRIMARY_DATE_OF_BIRTH);
            assertThat(accountHolder.isPrimaryAccountHolder()).isTrue();
            assertThat(accountHolder.getCreatedAt()).isEqualTo(CREATED_AT);
          });
    });
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldAddJointAccountHolder_whenBankAccountIsActive(AccountType accountType, Currency currency) {
    var bankAccount = openValidBankAccount(accountType, currency);
    bankAccount.activate();

    bankAccount.addJointAccountHolder(JOINT_ACCOUNT_HOLDER_NAME, JOINT_PASSPORT_NUMBER, JOINT_DATE_OF_BIRTH, CREATED_AT);

    assertThat(bankAccount)
      .satisfies(account -> assertThat(account.getAccountHolders())
        .hasSize(2)
        .filteredOn(AccountHolder::isJointAccountHolder)
        .hasSize(1)
        .singleElement()
        .satisfies(holder -> {
          assertThat(holder.getAccountHolderName()).isEqualTo(JOINT_ACCOUNT_HOLDER_NAME);
          assertThat(holder.getPassportNumber()).isEqualTo(JOINT_PASSPORT_NUMBER);
          assertThat(holder.getDateOfBirth()).isEqualTo(JOINT_DATE_OF_BIRTH);
          assertThat(holder.isJointAccountHolder()).isTrue();
        }));
  }

  @Test
  void shouldThrowBankAccountNotActiveException_whenAccountIsNotActive() {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);

    assertThatThrownBy(() -> addValidJointAccountHolder(bankAccount))
      .isInstanceOf(BankAccountNotActiveException.class);
  }

  @Test
  void shouldThrowMaxJointAccountHoldersExceededException_whenJointAccountHoldersLimitIsReached() {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);
    bankAccount.activate();

    addValidJointAccountHolder(bankAccount);

    assertThatThrownBy(() -> addValidJointAccountHolder(bankAccount))
      .isInstanceOf(MaxJointAccountHoldersExceededException.class);
  }

  @Test
  void shouldActivateBankAccount_whenStatusIsPending() {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);

    bankAccount.activate();

    assertThat(bankAccount.getStatus()).isEqualTo(AccountStatus.ACTIVE);
  }

  @Test
  void shouldNotThrowAnyException_whenActivateIsCalledTwice() {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);

    bankAccount.activate();
    bankAccount.activate();

    assertThat(bankAccount.getStatus().isActive()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(value = AccountStatus.class, names = { "PENDING", "ACTIVE" }, mode = EnumSource.Mode.EXCLUDE)
  void shouldThrowInvalidBankAccountStateTransitionException_whenActivatingFromInvalidState(AccountStatus status) {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);
    ReflectionTestUtils.setField(bankAccount, "status", status);

    assertThatThrownBy(bankAccount::activate)
      .isInstanceOf(InvalidBankAccountStateTransitionException.class);
  }

  @Test
  void shouldBlockBankAccount_whenStatusIsActive() {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);
    bankAccount.activate();

    bankAccount.block();
    assertThat(bankAccount.getStatus().isBlocked()).isTrue();
  }

  @Test
  void shouldNotThrowAnyException_whenBlockIsCalledTwice() {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);
    bankAccount.activate();

    bankAccount.block();
    bankAccount.block();

    assertThat(bankAccount.getStatus().isBlocked()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(value = AccountStatus.class, names = { "BLOCKED", "ACTIVE" }, mode = EnumSource.Mode.EXCLUDE)
  void shouldThrowInvalidBankAccountStateTransitionException_whenBlockingFromInvalidState(AccountStatus status) {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);
    ReflectionTestUtils.setField(bankAccount, "status", status);

    assertThatThrownBy(bankAccount::block)
      .isInstanceOf(InvalidBankAccountStateTransitionException.class);
  }

  @Test
  void shouldReturnAccountHolder_whenAccountHolderIsFound() {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);
    var primaryAccountHolder = bankAccount.primaryAccountHolder();

    assertThat(bankAccount.findAccountHolder(primaryAccountHolder.getAccountHolderId()))
      .hasValueSatisfying(holder -> assertThat(holder).isSameAs(primaryAccountHolder));
  }

  @Test
  void shouldReturnOnlyJointAccountHolders_whenJointAccountHoldersExist() {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);
    bankAccount.activate();
    addValidJointAccountHolder(bankAccount);

    assertThat(bankAccount.jointAccountHolders())
      .hasSize(1);
  }

  @Test
  void shouldReturnEmptyJointAccountHolders_whenNoJointAccountHoldersExist() {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);

    assertThat(bankAccount.jointAccountHolders()).isEmpty();
  }

  @Test
  void shouldReturnEmpty_whenAccountHolderIsNotFound() {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);
    assertThat(bankAccount.findAccountHolder(AccountHolderId.newId())).isEmpty();
  }

  @Test
  void shouldReturnEmpty_whenAccountHolderIdIsNull() {
    var bankAccount = openValidBankAccount(AccountType.CHECKING, Currency.USD);
    assertThat(bankAccount.findAccountHolder(null)).isEmpty();
  }

  private BankAccount openValidBankAccount(AccountType accountType, Currency currency) {
    return BankAccount.open(
      PRIMARY_ACCOUNT_HOLDER_NAME,
      PRIMARY_PASSPORT_NUMBER,
      PRIMARY_DATE_OF_BIRTH,
      accountType,
      currency,
      VALID_IBAN,
      CREATED_AT
    );
  }

  private void addValidJointAccountHolder(BankAccount bankAccount) {
    bankAccount.addJointAccountHolder(
      JOINT_ACCOUNT_HOLDER_NAME,
      JOINT_PASSPORT_NUMBER,
      JOINT_DATE_OF_BIRTH,
      CREATED_AT
    );
  }
}