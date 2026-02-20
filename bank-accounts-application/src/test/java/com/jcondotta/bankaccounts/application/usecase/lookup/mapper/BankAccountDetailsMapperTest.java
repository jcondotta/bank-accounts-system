package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountDetailsMapperTest {

  private static final BankAccountDetailsMapper mapper = new BankAccountDetailsMapperImpl(new AccountHolderDetailsMapperImpl());

  private static final Iban VALID_IBAN =
    Iban.of("ES3801283316232166447417");

  private static final AccountHolderName PRIMARY_ACCOUNT_HOLDER_NAME =
    AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber PRIMARY_PASSPORT_NUMBER =
    AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth PRIMARY_DATE_OF_BIRTH =
    AccountHolderFixtures.JEFFERSON.getDateOfBirth();
  private static final Email PRIMARY_EMAIL =
    AccountHolderFixtures.JEFFERSON.getEmail();

  private static final AccountHolderName JOINT_ACCOUNT_HOLDER_NAME =
    AccountHolderFixtures.PATRIZIO.getAccountHolderName();
  private static final PassportNumber JOINT_PASSPORT_NUMBER =
    AccountHolderFixtures.PATRIZIO.getPassportNumber();
  private static final DateOfBirth JOINT_DATE_OF_BIRTH =
    AccountHolderFixtures.PATRIZIO.getDateOfBirth();
  private static final Email JOINT_EMAIL =
    AccountHolderFixtures.PATRIZIO.getEmail();

  private static final Clock FIXED_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(FIXED_CLOCK);

  @Test
  void shouldMapBankAccountWithPrimaryAndJointHolders_whenAllFieldsArePresent() {
    BankAccount bankAccount = BankAccount.open(
      PRIMARY_ACCOUNT_HOLDER_NAME,
      PRIMARY_PASSPORT_NUMBER,
      PRIMARY_DATE_OF_BIRTH,
      PRIMARY_EMAIL,
      AccountType.CHECKING,
      Currency.EUR,
      VALID_IBAN
    );

    bankAccount.activate();

    bankAccount.addJointAccountHolder(
      JOINT_ACCOUNT_HOLDER_NAME,
      JOINT_PASSPORT_NUMBER,
      JOINT_DATE_OF_BIRTH,
      JOINT_EMAIL
    );

    BankAccountDetails details = mapper.toDetails(bankAccount);

    assertThat(details.bankAccountId()).isEqualTo(bankAccount.id());
    assertThat(details.accountType()).isEqualTo(bankAccount.accountType());
    assertThat(details.currency()).isEqualTo(bankAccount.currency());
    assertThat(details.iban()).isEqualTo(bankAccount.iban());
    assertThat(details.accountStatus()).isEqualTo(bankAccount.accountStatus());
    assertThat(details.createdAt()).isNotNull();

    List<AccountHolderDetails> holders = details.accountHolders();
    assertThat(holders).hasSize(2);

    AccountHolderDetails primaryHolder =
      holders.stream()
        .filter(h -> h.accountHolderType() == AccountHolderType.PRIMARY)
        .findFirst()
        .orElseThrow();

    assertThat(primaryHolder.name()).isEqualTo(PRIMARY_ACCOUNT_HOLDER_NAME);
    assertThat(primaryHolder.passportNumber()).isEqualTo(PRIMARY_PASSPORT_NUMBER);
    assertThat(primaryHolder.dateOfBirth()).isEqualTo(PRIMARY_DATE_OF_BIRTH);
    assertThat(primaryHolder.email()).isEqualTo(PRIMARY_EMAIL);
    assertThat(primaryHolder.createdAt()).isNotNull();

    AccountHolderDetails jointHolder =
      holders.stream()
        .filter(h -> h.accountHolderType() == AccountHolderType.JOINT)
        .findFirst()
        .orElseThrow();

    assertThat(jointHolder.name()).isEqualTo(JOINT_ACCOUNT_HOLDER_NAME);
    assertThat(jointHolder.passportNumber()).isEqualTo(JOINT_PASSPORT_NUMBER);
    assertThat(jointHolder.dateOfBirth()).isEqualTo(JOINT_DATE_OF_BIRTH);
    assertThat(jointHolder.email()).isEqualTo(JOINT_EMAIL);
    assertThat(jointHolder.createdAt()).isNotNull();
  }

  @Test
  void shouldReturnNull_whenBankAccountIsNull() {
    assertThat(mapper.toDetails(null)).isNull();
  }

  @Test
  void shouldReturnNull_whenAccountHoldersListIsNull() {
    BankAccountDetailsMapperImpl mapperImpl = (BankAccountDetailsMapperImpl) mapper;
    assertThat(mapperImpl.toDetails(null))
      .isNull();
  }
}
