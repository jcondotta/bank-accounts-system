package com.jcondotta.bankaccounts.application.usecase.openbankaccount.model;

import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OpenBankAccountCommandTest {

  private static final AccountHolderName ACCOUNT_HOLDER_NAME =
    AccountHolderFixtures.JEFFERSON.getAccountHolderName();

  private static final PassportNumber PASSPORT_NUMBER =
    AccountHolderFixtures.JEFFERSON.getPassportNumber();

  private static final DateOfBirth DATE_OF_BIRTH =
    AccountHolderFixtures.JEFFERSON.getDateOfBirth();

  private static final AccountType ACCOUNT_TYPE = AccountType.CHECKING;
  private static final Currency CURRENCY = Currency.EUR;

  @Test
  void shouldCreateCommand_whenAllFieldsAreProvided() {
    var command = OpenBankAccountCommand.of(ACCOUNT_HOLDER_NAME, PASSPORT_NUMBER, DATE_OF_BIRTH, ACCOUNT_TYPE, CURRENCY);

    assertThat(command.accountHolderName()).isEqualTo(ACCOUNT_HOLDER_NAME);
    assertThat(command.passportNumber()).isEqualTo(PASSPORT_NUMBER);
    assertThat(command.dateOfBirth()).isEqualTo(DATE_OF_BIRTH);
    assertThat(command.accountType()).isEqualTo(ACCOUNT_TYPE);
    assertThat(command.currency()).isEqualTo(CURRENCY);
  }

  @Test
  void shouldThrowNullPointerException_whenAccountHolderNameIsNull() {
    assertThatThrownBy(
      () -> new OpenBankAccountCommand(null, PASSPORT_NUMBER, DATE_OF_BIRTH, ACCOUNT_TYPE, CURRENCY))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("accountHolderName must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenPassportNumberIsNull() {
    assertThatThrownBy(
      () -> new OpenBankAccountCommand(ACCOUNT_HOLDER_NAME, null, DATE_OF_BIRTH, ACCOUNT_TYPE, CURRENCY))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("passportNumber must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenDateOfBirthIsNull() {
    assertThatThrownBy(
      () -> new OpenBankAccountCommand(ACCOUNT_HOLDER_NAME, PASSPORT_NUMBER, null, ACCOUNT_TYPE, CURRENCY))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("dateOfBirth must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenAccountTypeIsNull() {
    assertThatThrownBy(
      () -> new OpenBankAccountCommand(ACCOUNT_HOLDER_NAME, PASSPORT_NUMBER, DATE_OF_BIRTH, null, CURRENCY))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("accountType must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenCurrencyIsNull() {
    assertThatThrownBy(
      () -> new OpenBankAccountCommand(ACCOUNT_HOLDER_NAME, PASSPORT_NUMBER, DATE_OF_BIRTH, ACCOUNT_TYPE, null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("currency must not be null");
  }
}
