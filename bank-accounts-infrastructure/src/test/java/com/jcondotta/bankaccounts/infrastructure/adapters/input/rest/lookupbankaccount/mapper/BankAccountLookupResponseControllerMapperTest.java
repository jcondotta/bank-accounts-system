package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.AccountHolderDetailsResponse;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.BankAccountDetailsResponse;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.model.BankAccountLookupResponse;
import com.jcondotta.bankaccounts.infrastructure.config.ClockTestFactory;
import com.jcondotta.bankaccounts.infrastructure.fixtures.AccountHolderFixtures;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountLookupResponseControllerMapperTest {

  private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();
  private static final AccountType ACCOUNT_TYPE_CHECKING = AccountType.CHECKING;
  private static final Currency CURRENCY_EUR = Currency.EUR;
  private static final AccountStatus ACCOUNT_STATUS_ACTIVE = AccountStatus.ACTIVE;

  private static final AccountHolderId ACCOUNT_HOLDER_ID = AccountHolderId.newId();

  private static final AccountHolderName VALID_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber VALID_PASSPORT = AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth VALID_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth();

  private static final Clock FIXED_CLOCK = ClockTestFactory.TEST_CLOCK_FIXED;
  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(FIXED_CLOCK);


  public static final Iban VALID_IBAN = Iban.of("ES3801283316232166447417");

  private final BankAccountLookupResponseControllerMapper mapper =
    new BankAccountLookupResponseControllerMapperImpl(new AccountHolderDetailsResponseMapperImpl());

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldMapBankAccountDetailsToLookupResponse_whenValuesAreValid(AccountHolderType accountHolderType) {
    AccountHolderDetails accountHolderDetails = new AccountHolderDetails(
      ACCOUNT_HOLDER_ID,
      VALID_NAME,
      VALID_PASSPORT,
      VALID_DATE_OF_BIRTH,
      accountHolderType,
      CREATED_AT
    );

    BankAccountDetails bankAccountDetails = new BankAccountDetails(
      BANK_ACCOUNT_ID,
      ACCOUNT_TYPE_CHECKING,
      CURRENCY_EUR,
      VALID_IBAN,
      ACCOUNT_STATUS_ACTIVE,
      CREATED_AT,
      List.of(accountHolderDetails)
    );

    BankAccountLookupResponse response = mapper.toResponse(bankAccountDetails);

    assertThat(response).isNotNull();

    BankAccountDetailsResponse details = response.bankAccount();
    assertThat(details.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID.value());
    assertThat(details.accountType()).isEqualTo(ACCOUNT_TYPE_CHECKING);
    assertThat(details.currency()).isEqualTo(CURRENCY_EUR);
    assertThat(details.iban()).isEqualTo(VALID_IBAN.value());
    assertThat(details.openingDate()).isEqualTo(CREATED_AT);
    assertThat(details.accountStatus()).isEqualTo(ACCOUNT_STATUS_ACTIVE);

    assertThat(details.accountHolders()).hasSize(1);
    AccountHolderDetailsResponse holder = details.accountHolders().getFirst();
    assertThat(holder.accountHolderId()).isEqualTo(ACCOUNT_HOLDER_ID.value());
    assertThat(holder.accountHolderName()).isEqualTo(VALID_NAME.value());
    assertThat(holder.passportNumber()).isEqualTo(VALID_PASSPORT.value());
    assertThat(holder.dateOfBirth()).isEqualTo(VALID_DATE_OF_BIRTH.value());
    assertThat(holder.accountHolderType()).isEqualTo(accountHolderType);
    assertThat(holder.createdAt()).isEqualTo(CREATED_AT);
  }
}