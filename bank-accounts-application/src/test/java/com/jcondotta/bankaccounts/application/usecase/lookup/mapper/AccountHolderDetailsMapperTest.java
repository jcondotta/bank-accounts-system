package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.domain.entities.AccountHolder;
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

import static org.assertj.core.api.Assertions.assertThat;

class AccountHolderDetailsMapperTest {

  private static final AccountHolderDetailsMapper mapper =
    Mappers.getMapper(AccountHolderDetailsMapper.class);

  private static final Iban VALID_IBAN =
    Iban.of("ES3801283316232166447417");

  private static final AccountHolderName ACCOUNT_HOLDER_NAME =
    AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber PASSPORT_NUMBER =
    AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth DATE_OF_BIRTH =
    AccountHolderFixtures.JEFFERSON.getDateOfBirth();

  private static final Clock FIXED_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(FIXED_CLOCK);

  @Test
  void shouldMapAccountHolderToAccountHolderDetails_whenAllFieldsArePresent() {
    BankAccount bankAccount = BankAccount.open(
      ACCOUNT_HOLDER_NAME,
      PASSPORT_NUMBER,
      DATE_OF_BIRTH,
      AccountType.CHECKING,
      Currency.EUR,
      VALID_IBAN,
      CREATED_AT
    );

    AccountHolder accountHolder = bankAccount.primaryAccountHolder();
    AccountHolderDetails details = mapper.toDetails(accountHolder);

    assertThat(details.accountHolderId()).isEqualTo(accountHolder.getAccountHolderId());
    assertThat(details.accountHolderName()).isEqualTo(accountHolder.getAccountHolderName());
    assertThat(details.passportNumber()).isEqualTo(accountHolder.getPassportNumber());
    assertThat(details.dateOfBirth()).isEqualTo(accountHolder.getDateOfBirth());
    assertThat(details.accountHolderType()).isEqualTo(AccountHolderType.PRIMARY);
    assertThat(details.createdAt()).isEqualTo(CREATED_AT);
  }

  @Test
  void shouldReturnNull_whenAccountHolderIsNull() {
    AccountHolderDetails details = mapper.toDetails(null);

    assertThat(details).isNull();
  }
}