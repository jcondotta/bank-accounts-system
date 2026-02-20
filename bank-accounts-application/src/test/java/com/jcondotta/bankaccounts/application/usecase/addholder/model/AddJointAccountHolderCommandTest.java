package com.jcondotta.bankaccounts.application.usecase.addholder.model;

import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddJointAccountHolderCommandTest {

  private static final AccountHolderName ACCOUNT_HOLDER_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber PASSPORT_NUMBER = AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth();
  private static final Email EMAIL = AccountHolderFixtures.JEFFERSON.getEmail();

  @Test
  void shouldCreateCommand_whenAllFieldsAreProvided() {
    var bankAccountId = BankAccountId.newId();

    var command = new AddJointAccountHolderCommand(bankAccountId, ACCOUNT_HOLDER_NAME, PASSPORT_NUMBER, DATE_OF_BIRTH, EMAIL);

    assertThat(command.bankAccountId()).isEqualTo(bankAccountId);
    assertThat(command.name()).isEqualTo(ACCOUNT_HOLDER_NAME);
    assertThat(command.passportNumber()).isEqualTo(PASSPORT_NUMBER);
    assertThat(command.dateOfBirth()).isEqualTo(DATE_OF_BIRTH);
    assertThat(command.email()).isEqualTo(EMAIL);
  }

  @Test
  void shouldThrowNullPointerException_whenBankAccountIdIsNull() {
    assertThatThrownBy(
      () -> new AddJointAccountHolderCommand(null, ACCOUNT_HOLDER_NAME, PASSPORT_NUMBER, DATE_OF_BIRTH, EMAIL))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("bankAccountId must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenAccountHolderNameIsNull() {
    assertThatThrownBy(() -> new AddJointAccountHolderCommand(BankAccountId.newId(), null, PASSPORT_NUMBER, DATE_OF_BIRTH, EMAIL))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("name must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenPassportNumberIsNull() {
    assertThatThrownBy(() -> new AddJointAccountHolderCommand(BankAccountId.newId(), ACCOUNT_HOLDER_NAME, null, DATE_OF_BIRTH, EMAIL))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("passportNumber must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenDateOfBirthIsNull() {
    assertThatThrownBy(() -> new AddJointAccountHolderCommand(BankAccountId.newId(), ACCOUNT_HOLDER_NAME, PASSPORT_NUMBER, null, EMAIL))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("dateOfBirth must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenEmailIsNull() {
    assertThatThrownBy(() -> new AddJointAccountHolderCommand(BankAccountId.newId(), ACCOUNT_HOLDER_NAME, PASSPORT_NUMBER, DATE_OF_BIRTH, null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("email must not be null");
  }
}