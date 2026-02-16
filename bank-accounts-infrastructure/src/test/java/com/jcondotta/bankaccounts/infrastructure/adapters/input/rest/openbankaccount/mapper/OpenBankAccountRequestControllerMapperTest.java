package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.mapper;

import com.jcondotta.bankaccounts.application.usecase.open.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.OpenBankAccountRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.PrimaryAccountHolderRequest;
import com.jcondotta.bankaccounts.infrastructure.arguments_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.infrastructure.fixtures.AccountHolderFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class OpenBankAccountRequestControllerMapperTest {

  private static final String VALID_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName().value();
  private static final String VALID_PASSPORT = AccountHolderFixtures.JEFFERSON.getPassportNumber().value();
  private static final LocalDate VALID_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth().value();

  private final OpenBankAccountRequestControllerMapper mapper = Mappers.getMapper(OpenBankAccountRequestControllerMapper.class);

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldMapOpenBankAccountRequestToCommand_whenValueAreValid(AccountType accountType, Currency currency) {
    var accountHolderRequest = new PrimaryAccountHolderRequest(
        VALID_NAME,
        VALID_PASSPORT,
        VALID_DATE_OF_BIRTH
      );

    OpenBankAccountRequest request = new OpenBankAccountRequest(
        accountType,
        currency,
        accountHolderRequest
      );

    OpenBankAccountCommand command = mapper.toCommand(request);

    assertThat(command).isNotNull();
    assertThat(command.accountHolderName().value()).isEqualTo(VALID_NAME);
    assertThat(command.passportNumber().value()).isEqualTo(VALID_PASSPORT);
    assertThat(command.dateOfBirth().value()).isEqualTo(VALID_DATE_OF_BIRTH);

    assertThat(command.accountType()).isEqualTo(accountType);
    assertThat(command.currency()).isEqualTo(currency);
  }

  @Test
  void shouldConvertStringToAccountHolderName_whenValueIsValid() {
    AccountHolderName accountHolderName = mapper.toAccountHolderName(VALID_NAME);

    assertThat(accountHolderName.value()).isEqualTo(VALID_NAME);
  }

  @Test
  void shouldConvertStringToPassportNumber_whenValueIsValid() {
    PassportNumber passportNumber = mapper.toPassportNumber(VALID_PASSPORT);

    assertThat(passportNumber.value()).isEqualTo(VALID_PASSPORT);
  }

  @Test
  void shouldConvertLocalDateToDateOfBirth_whenValueIsValid() {
    DateOfBirth dateOfBirth = mapper.toDateOfBirth(VALID_DATE_OF_BIRTH);

    assertThat(dateOfBirth.value()).isEqualTo(VALID_DATE_OF_BIRTH);
  }

  @Test
  void shouldReturn_whenMappedRequestIsNull() {
    OpenBankAccountCommand command = mapper.toCommand(null);
    assertThat(command).isNull();
  }
}
