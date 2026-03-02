package com.jcondotta.bankaccounts.application.usecase.open.model;

import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.address.Address;
import com.jcondotta.bankaccounts.domain.value_objects.contact.ContactInfo;
import com.jcondotta.bankaccounts.domain.value_objects.personal.PersonalInfo;
import org.junit.jupiter.api.Test;

import static com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures.JEFFERSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OpenBankAccountCommandTest {

  private static final PersonalInfo PERSONAL_INFO = JEFFERSON.personalInfo();
  private static final ContactInfo CONTACT_INFO = JEFFERSON.contactInfo();
  private static final Address ADDRESS = JEFFERSON.address();

  private static final AccountType ACCOUNT_TYPE = AccountType.CHECKING;
  private static final Currency CURRENCY = Currency.EUR;

  @Test
  void shouldCreateCommand_whenAllFieldsAreProvided() {
    var command = new OpenBankAccountCommand(
      PERSONAL_INFO,
      CONTACT_INFO,
      ADDRESS,
      ACCOUNT_TYPE,
      CURRENCY
    );

    assertThat(command.personalInfo()).isEqualTo(PERSONAL_INFO);
    assertThat(command.contactInfo()).isEqualTo(CONTACT_INFO);
    assertThat(command.address()).isEqualTo(ADDRESS);
    assertThat(command.accountType()).isEqualTo(ACCOUNT_TYPE);
    assertThat(command.currency()).isEqualTo(CURRENCY);
  }

  @Test
  void shouldThrowNullPointerException_whenPersonalInfoIsNull() {
    assertThatThrownBy(() -> new OpenBankAccountCommand(null, CONTACT_INFO, ADDRESS, ACCOUNT_TYPE, CURRENCY))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("personalInfo must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenContactInfoIsNull() {
    assertThatThrownBy(() -> new OpenBankAccountCommand(PERSONAL_INFO, null, ADDRESS, ACCOUNT_TYPE, CURRENCY))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("contactInfo must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenAddressIsNull() {
    assertThatThrownBy(() -> new OpenBankAccountCommand(PERSONAL_INFO, CONTACT_INFO, null, ACCOUNT_TYPE, CURRENCY))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("address must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenAccountTypeIsNull() {
    assertThatThrownBy(() -> new OpenBankAccountCommand(PERSONAL_INFO, CONTACT_INFO, ADDRESS, null, CURRENCY))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("accountType must not be null");
  }

  @Test
  void shouldThrowNullPointerException_whenCurrencyIsNull() {
    assertThatThrownBy(() -> new OpenBankAccountCommand(PERSONAL_INFO, CONTACT_INFO, ADDRESS, ACCOUNT_TYPE, null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("currency must not be null");
  }
}