package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.AccountHolderDetailsResponse;
import com.jcondotta.bankaccounts.infrastructure.config.ClockTestFactory;
import com.jcondotta.bankaccounts.infrastructure.fixtures.AccountHolderFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mapstruct.factory.Mappers;

import java.time.Clock;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AccountHolderDetailsResponseMapperTest {

  private static final AccountHolderId ACCOUNT_HOLDER_ID = AccountHolderId.newId();

  private static final AccountHolderName VALID_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber VALID_PASSPORT = AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth VALID_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth();

  private static final Clock FIXED_CLOCK = ClockTestFactory.TEST_CLOCK_FIXED;
  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(FIXED_CLOCK);

  private final AccountHolderDetailsResponseMapper mapper =
    Mappers.getMapper(AccountHolderDetailsResponseMapper.class);

  @ParameterizedTest
  @EnumSource(AccountHolderType.class)
  void shouldMapAccountHolderDetailsToResponse_whenValuesAreValid(AccountHolderType accountHolderType) {
    AccountHolderDetails details = new AccountHolderDetails(
      ACCOUNT_HOLDER_ID,
      VALID_NAME,
      VALID_PASSPORT,
      VALID_DATE_OF_BIRTH,
      accountHolderType,
      CREATED_AT
    );

    AccountHolderDetailsResponse response = mapper.toResponse(details);

    assertThat(response).isNotNull();
    assertThat(response.accountHolderId()).isEqualTo(ACCOUNT_HOLDER_ID.value());
    assertThat(response.accountHolderName()).isEqualTo(VALID_NAME.value());
    assertThat(response.passportNumber()).isEqualTo(VALID_PASSPORT.value());
    assertThat(response.dateOfBirth()).isEqualTo(VALID_DATE_OF_BIRTH.value());
    assertThat(response.accountHolderType()).isEqualTo(accountHolderType);
    assertThat(response.createdAt()).isEqualTo(CREATED_AT);
  }

  @Test
  void shouldReturnNull_whenAccountHolderDetailsIsNull() {
    AccountHolderDetailsResponse response = mapper.toResponse(null);

    assertThat(response).isNull();
  }
}