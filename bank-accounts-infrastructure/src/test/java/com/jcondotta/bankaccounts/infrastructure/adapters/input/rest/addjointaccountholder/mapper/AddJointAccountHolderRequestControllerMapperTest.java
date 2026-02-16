package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.mapper;

import com.jcondotta.bankaccounts.application.usecase.addholder.model.AddJointAccountHolderCommand;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.model.AddJointAccountHolderRequest;
import com.jcondotta.bankaccounts.infrastructure.fixtures.AccountHolderFixtures;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AddJointAccountHolderRequestControllerMapperTest {

  private static final UUID BANK_ACCOUNT_UUID = BankAccountId.newId().value();

  private static final String VALID_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName().value();
  private static final String VALID_PASSPORT = AccountHolderFixtures.JEFFERSON.getPassportNumber().value();
  private static final LocalDate VALID_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth().value();

  private final AddJointAccountHolderRequestControllerMapper mapper =
    Mappers.getMapper(AddJointAccountHolderRequestControllerMapper.class);

  @Test
  void shouldMapAddJointAccountHolderRequestToCommand_whenValuesAreValid() {
    AddJointAccountHolderRequest request =
      new AddJointAccountHolderRequest(
        VALID_NAME,
        VALID_DATE_OF_BIRTH,
        VALID_PASSPORT
      );

    AddJointAccountHolderCommand command =
      mapper.toCommand(BANK_ACCOUNT_UUID, request);

    assertThat(command).isNotNull();
    assertThat(command.bankAccountId().value()).isEqualTo(BANK_ACCOUNT_UUID);
    assertThat(command.accountHolderName().value()).isEqualTo(VALID_NAME);
    assertThat(command.passportNumber().value()).isEqualTo(VALID_PASSPORT);
    assertThat(command.dateOfBirth().value()).isEqualTo(VALID_DATE_OF_BIRTH);
  }

  @Test
  void shouldConvertUuidToBankAccountId_whenValueIsValid() {
    BankAccountId bankAccountId = mapper.toBankAccountId(BANK_ACCOUNT_UUID);

    assertThat(bankAccountId.value()).isEqualTo(BANK_ACCOUNT_UUID);
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
}