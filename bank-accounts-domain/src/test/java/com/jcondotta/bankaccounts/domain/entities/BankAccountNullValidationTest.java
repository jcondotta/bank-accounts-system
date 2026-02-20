package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.domain.validation.AccountHolderValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.BankAccountValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountNullValidationTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;

  private static final Iban VALID_IBAN = BankAccountTestFixture.VALID_IBAN;
  private static final AccountType ACCOUNT_TYPE_SAVINGS = AccountType.SAVINGS;
  private static final Currency CURRENCY_USD = Currency.USD;

  @Test
  void shouldThrowNullPointerException_whenAccountHolderNameIsNull() {
    assertThatThrownBy(() ->
      BankAccount.open(
        null,
        PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
        PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
        PRIMARY_ACCOUNT_HOLDER.getEmail(),
        ACCOUNT_TYPE_SAVINGS,
        CURRENCY_USD,
        VALID_IBAN
      ))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.NAME_NOT_NULL);
  }

  @Test
  void shouldThrowNullPointerException_whenPassportNumberIsNull() {
    assertThatThrownBy(() ->
      BankAccount.open(
        PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
        null,
        PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
        PRIMARY_ACCOUNT_HOLDER.getEmail(),
        ACCOUNT_TYPE_SAVINGS,
        CURRENCY_USD,
        VALID_IBAN
      ))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.PASSPORT_NUMBER_NOT_NULL);
  }

  @Test
  void shouldThrowNullPointerException_whenDateOfBirthIsNull() {
    assertThatThrownBy(() ->
      BankAccount.open(
        PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
        PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
        null,
        PRIMARY_ACCOUNT_HOLDER.getEmail(),
        ACCOUNT_TYPE_SAVINGS,
        CURRENCY_USD,
        VALID_IBAN
      ))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.DATE_OF_BIRTH_NOT_NULL);
  }

  @Test
  void shouldThrowNullPointerException_whenEmailIsNull() {
    assertThatThrownBy(() ->
      BankAccount.open(
        PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
        PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
        PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
        null,
        ACCOUNT_TYPE_SAVINGS,
        CURRENCY_USD,
        VALID_IBAN
      ))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(AccountHolderValidationErrors.EMAIL_NOT_NULL);
  }

  @Test
  void shouldThrowNullPointerException_whenAccountTypeIsNull() {
    assertThatThrownBy(() ->
      BankAccount.open(
        PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
        PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
        PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
        PRIMARY_ACCOUNT_HOLDER.getEmail(),
        null,
        CURRENCY_USD,
        VALID_IBAN
      ))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(BankAccountValidationErrors.ACCOUNT_TYPE_NOT_NULL);
  }

  @Test
  void shouldThrowNullPointerException_whenCurrencyIsNull() {
    assertThatThrownBy(() ->
      BankAccount.open(
        PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
        PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
        PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
        PRIMARY_ACCOUNT_HOLDER.getEmail(),
        ACCOUNT_TYPE_SAVINGS,
        null,
        VALID_IBAN
      ))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(BankAccountValidationErrors.CURRENCY_NOT_NULL);
  }

  @Test
  void shouldThrowNullPointerException_whenIbanIsNull() {
    assertThatThrownBy(() ->
      BankAccount.open(
        PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
        PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
        PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
        PRIMARY_ACCOUNT_HOLDER.getEmail(),
        ACCOUNT_TYPE_SAVINGS,
        CURRENCY_USD,
        null
      ))
      .isInstanceOf(NullPointerException.class)
      .hasMessage(BankAccountValidationErrors.IBAN_NOT_NULL);
  }
}