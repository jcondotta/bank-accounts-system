package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.arguments_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.exceptions.InvalidBankAccountHoldersConfigurationException;
import com.jcondotta.bankaccounts.domain.exceptions.MaxJointAccountHoldersExceededException;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountRestoreTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;
  private static final AccountHolderFixtures JOINT_ACCOUNT_HOLDER = AccountHolderFixtures.PATRIZIO;

  private static final Iban VALID_IBAN = BankAccountTestFixture.VALID_IBAN;
  private static final AccountType ACCOUNT_TYPE_SAVINGS = AccountType.SAVINGS;
  private static final Currency CURRENCY_USD = Currency.USD;

  private static final Instant ACCOUNT_CREATED_AT = Instant.now(ClockTestFactory.FIXED_CLOCK);

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldRestoreBankAccountWithPrimaryAccountHolder_whenValidDataProvided(AccountType accountType, Currency currency) {
    var primaryAccountHolder = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);

    var bankAccountId = BankAccountId.newId();
    var bankAccount = BankAccount.restore(
      bankAccountId,
      accountType,
      currency,
      VALID_IBAN,
      AccountStatus.ACTIVE,
      ACCOUNT_CREATED_AT,
      List.of(primaryAccountHolder)
    );

    assertThat(bankAccount).isNotNull();
    assertThat(bankAccount.id()).isEqualTo(bankAccountId);
    assertThat(bankAccount.accountType()).isEqualTo(accountType);
    assertThat(bankAccount.currency()).isEqualTo(currency);
    assertThat(bankAccount.iban()).isEqualTo(BankAccountTestFixture.VALID_IBAN);
    assertThat(bankAccount.accountStatus().isActive()).isTrue();
    assertThat(bankAccount.createdAt()).isEqualTo(ACCOUNT_CREATED_AT);
    assertThat(bankAccount.pullEvents()).isEmpty();
    assertThat(bankAccount.accountHolders())
      .hasSize(1)
      .containsExactly(primaryAccountHolder);
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldRestoreBankAccountWithPrimaryAndJointAccountHolders_whenValidDataProvided(AccountType accountType, Currency currency) {
    var primaryAccountHolder = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);
    var jointAccountHolder = BankAccountTestFixture.createJointHolder(JOINT_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);

    var bankAccountId = BankAccountId.newId();
    var bankAccount = BankAccount.restore(
      bankAccountId,
      accountType,
      currency,
      BankAccountTestFixture.VALID_IBAN,
      AccountStatus.ACTIVE,
      ACCOUNT_CREATED_AT,
      List.of(primaryAccountHolder, jointAccountHolder)
    );

    assertThat(bankAccount).isNotNull();
    assertThat(bankAccount.id()).isEqualTo(bankAccountId);
    assertThat(bankAccount.accountType()).isEqualTo(accountType);
    assertThat(bankAccount.currency()).isEqualTo(currency);
    assertThat(bankAccount.iban()).isEqualTo(BankAccountTestFixture.VALID_IBAN);
    assertThat(bankAccount.accountStatus().isActive()).isTrue();
    assertThat(bankAccount.createdAt()).isEqualTo(ACCOUNT_CREATED_AT);
    assertThat(bankAccount.pullEvents()).isEmpty();
    assertThat(bankAccount.accountHolders())
      .hasSize(2)
      .containsExactlyInAnyOrder(primaryAccountHolder, jointAccountHolder);
  }

  @Test
  void shouldThrowInvalidBankAccountHoldersConfigurationException_whenRestoringWithoutAnyAccountHolders() {
    assertThatThrownBy(() ->
      BankAccount.restore(
        BankAccountId.newId(),
        ACCOUNT_TYPE_SAVINGS,
        CURRENCY_USD,
        BankAccountTestFixture.VALID_IBAN,
        AccountStatus.ACTIVE,
        ACCOUNT_CREATED_AT,
        List.of()
      ))
      .isInstanceOf(InvalidBankAccountHoldersConfigurationException.class);
  }

  @Test
  void shouldThrowInvalidBankAccountHoldersConfigurationException_whenRestoringWithOnlyJointAccountHolder() {
    var jointAccountHolder = BankAccountTestFixture.createJointHolder(JOINT_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);

    assertThatThrownBy(() ->
      BankAccount.restore(
        BankAccountId.newId(),
        ACCOUNT_TYPE_SAVINGS,
        CURRENCY_USD,
        BankAccountTestFixture.VALID_IBAN,
        AccountStatus.ACTIVE,
        ACCOUNT_CREATED_AT,
        List.of(jointAccountHolder)
      ))
      .isInstanceOf(InvalidBankAccountHoldersConfigurationException.class);
  }

  @Test
  void shouldThrowException_whenRestoringWithMultiplePrimaryAccountHolders() {
    var primaryAccountHolder1 = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);
    var primaryAccountHolder2 = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);

    assertThatThrownBy(() ->
      BankAccount.restore(
        BankAccountId.newId(),
        ACCOUNT_TYPE_SAVINGS,
        CURRENCY_USD,
        BankAccountTestFixture.VALID_IBAN,
        AccountStatus.ACTIVE,
        ACCOUNT_CREATED_AT,
        List.of(primaryAccountHolder1, primaryAccountHolder2)
      ))
      .isInstanceOf(InvalidBankAccountHoldersConfigurationException.class);
  }

  @Test
  void shouldThrowMaxJointAccountHoldersExceededException_whenRestoringWithPrimaryAndMultipleJointAccountHolders_ifOnlyOneJointIsAllowed() {
    var primaryAccountHolder = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);
    var jointAccountHolder1 = BankAccountTestFixture.createJointHolder(JOINT_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);
    var jointAccountHolder2 = BankAccountTestFixture.createJointHolder(JOINT_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);

    assertThatThrownBy(() ->
      BankAccount.restore(
        BankAccountId.newId(),
        ACCOUNT_TYPE_SAVINGS,
        CURRENCY_USD,
        BankAccountTestFixture.VALID_IBAN,
        AccountStatus.ACTIVE,
        ACCOUNT_CREATED_AT,
        List.of(primaryAccountHolder, jointAccountHolder1, jointAccountHolder2)
      ))
      .isInstanceOf(MaxJointAccountHoldersExceededException.class);
  }
}
