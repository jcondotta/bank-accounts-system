package com.jcondotta.bankaccounts.application.usecase.addholder.model;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.address.Address;
import com.jcondotta.bankaccounts.domain.value_objects.contact.ContactInfo;
import com.jcondotta.bankaccounts.domain.value_objects.personal.PersonalInfo;
import org.junit.jupiter.api.Test;

import static com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures.JEFFERSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddJointAccountHolderCommandTest {

  private static final PersonalInfo PERSONAL_INFO = JEFFERSON.personalInfo();
  private static final ContactInfo CONTACT_INFO = JEFFERSON.contactInfo();
  private static final Address ADDRESS = JEFFERSON.address();

  @Test
  void shouldCreateCommand_whenAllFieldsAreProvided() {
    var bankAccountId = BankAccountId.newId();

    var command = new AddJointAccountHolderCommand(
      bankAccountId,
      PERSONAL_INFO,
      CONTACT_INFO,
      ADDRESS
    );

    assertThat(command.bankAccountId()).isEqualTo(bankAccountId);
    assertThat(command.personalInfo()).isEqualTo(PERSONAL_INFO);
    assertThat(command.contactInfo()).isEqualTo(CONTACT_INFO);
    assertThat(command.address()).isEqualTo(ADDRESS);
  }

  @Test
  void shouldThrowNullPointerException_whenBankAccountIdIsNull() {
    assertThatThrownBy(
      () -> new AddJointAccountHolderCommand(
        null,
        PERSONAL_INFO,
        CONTACT_INFO,
        ADDRESS
      ))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("bankAccountId must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenPersonalInfoIsNull() {
    assertThatThrownBy(
      () -> new AddJointAccountHolderCommand(
        BankAccountId.newId(),
        null,
        CONTACT_INFO,
        ADDRESS
      ))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("personalInfo must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenContactInfoIsNull() {
    assertThatThrownBy(
      () -> new AddJointAccountHolderCommand(
        BankAccountId.newId(),
        PERSONAL_INFO,
        null,
        ADDRESS
      ))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("contactInfo must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenAddressIsNull() {
    assertThatThrownBy(
      () -> new AddJointAccountHolderCommand(
        BankAccountId.newId(),
        PERSONAL_INFO,
        CONTACT_INFO,
        null
      ))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("address must not be null");
  }
}