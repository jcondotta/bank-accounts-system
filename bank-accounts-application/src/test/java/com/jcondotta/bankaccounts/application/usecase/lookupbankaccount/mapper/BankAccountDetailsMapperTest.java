package com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountDetailsMapperTest {

  private static final BankAccountDetailsMapper mapper =
    Mappers.getMapper(BankAccountDetailsMapper.class);

  private static final Iban VALID_IBAN =
    Iban.of("ES3801283316232166447417");

  private static final AccountHolderName PRIMARY_ACCOUNT_HOLDER_NAME =
    AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber PRIMARY_PASSPORT_NUMBER =
    AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth PRIMARY_DATE_OF_BIRTH =
    AccountHolderFixtures.JEFFERSON.getDateOfBirth();

  private static final AccountHolderName JOINT_ACCOUNT_HOLDER_NAME =
    AccountHolderFixtures.PATRIZIO.getAccountHolderName();
  private static final PassportNumber JOINT_PASSPORT_NUMBER =
    AccountHolderFixtures.PATRIZIO.getPassportNumber();
  private static final DateOfBirth JOINT_DATE_OF_BIRTH =
    AccountHolderFixtures.PATRIZIO.getDateOfBirth();

  private static final Clock FIXED_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(FIXED_CLOCK);

  @Test
  void shouldMapBankAccountWithPrimaryAndJointHolders_whenAllFieldsArePresent() {
    BankAccount bankAccount = BankAccount.open(
      PRIMARY_ACCOUNT_HOLDER_NAME,
      PRIMARY_PASSPORT_NUMBER,
      PRIMARY_DATE_OF_BIRTH,
      AccountType.CHECKING,
      Currency.EUR,
      VALID_IBAN,
      CREATED_AT
    );

    bankAccount.activate();

    bankAccount.addJointAccountHolder(
      JOINT_ACCOUNT_HOLDER_NAME,
      JOINT_PASSPORT_NUMBER,
      JOINT_DATE_OF_BIRTH,
      CREATED_AT
    );

    BankAccountDetails details = mapper.toDetails(bankAccount);

    assertThat(details.bankAccountId()).isEqualTo(bankAccount.getBankAccountId());
    assertThat(details.accountType()).isEqualTo(bankAccount.getAccountType());
    assertThat(details.currency()).isEqualTo(bankAccount.getCurrency());
    assertThat(details.iban()).isEqualTo(bankAccount.getIban());
    assertThat(details.accountStatus()).isEqualTo(bankAccount.getStatus());
    assertThat(details.openingDate()).isEqualTo(bankAccount.getCreatedAt());

    List<AccountHolderDetails> holders = details.accountHolders();
    assertThat(holders).hasSize(2);

    AccountHolderDetails primaryHolder =
      holders.stream()
        .filter(h -> h.accountHolderType() == AccountHolderType.PRIMARY)
        .findFirst()
        .orElseThrow();

    assertThat(primaryHolder.accountHolderName()).isEqualTo(PRIMARY_ACCOUNT_HOLDER_NAME);
    assertThat(primaryHolder.passportNumber()).isEqualTo(PRIMARY_PASSPORT_NUMBER);
    assertThat(primaryHolder.dateOfBirth()).isEqualTo(PRIMARY_DATE_OF_BIRTH);
    assertThat(primaryHolder.createdAt()).isEqualTo(CREATED_AT);

    AccountHolderDetails jointHolder =
      holders.stream()
        .filter(h -> h.accountHolderType() == AccountHolderType.JOINT)
        .findFirst()
        .orElseThrow();

    assertThat(jointHolder.accountHolderName()).isEqualTo(JOINT_ACCOUNT_HOLDER_NAME);
    assertThat(jointHolder.passportNumber()).isEqualTo(JOINT_PASSPORT_NUMBER);
    assertThat(jointHolder.dateOfBirth()).isEqualTo(JOINT_DATE_OF_BIRTH);
    assertThat(jointHolder.createdAt()).isEqualTo(CREATED_AT);
  }

//  @Test
//  void shouldReturnNull_whenBankAccountIsNull() {
//    var bankAccount = Instancio.of(BankAccount.class)
//      .set(field(BankAccount::getAccountHolders), null)
//      .create();
//
//    BankAccountDetails details = mapper.toDetails(bankAccount);
//
//    assertThat(details.accountHolders()).isNull();
//  }
}
