package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class AccountHolderDetailsMapperTest {

  private final AccountHolderDetailsMapper mapper = Mappers.getMapper(AccountHolderDetailsMapper.class);

  @Test
  void shouldMapAccountHolderToDetails_whenAllFieldsArePresent() {
    var accountHolder = BankAccountTestFixture.createPrimaryHolder(AccountHolderFixtures.JEFFERSON, Instant.now());

    AccountHolderDetails accountHolderDetails = mapper.toDetails(accountHolder);

    assertThat(accountHolderDetails.id()).isEqualTo(accountHolder.id().value());

    assertThat(accountHolderDetails.firstName()).isEqualTo(accountHolder.personalInfo().holderName().firstName());

    assertThat(accountHolderDetails.lastName()).isEqualTo(accountHolder.personalInfo().holderName().lastName());

    assertThat(accountHolderDetails.documentType())
      .isEqualTo(accountHolder.personalInfo().identityDocument().type().toString());

    assertThat(accountHolderDetails.documentNumber())
      .isEqualTo(accountHolder.personalInfo().identityDocument().number().value());

    assertThat(accountHolderDetails.dateOfBirth())
      .isEqualTo(accountHolder.personalInfo().dateOfBirth().value());

    assertThat(accountHolderDetails.email())
      .isEqualTo(accountHolder.contactInfo().email().value());

    assertThat(accountHolderDetails.phoneNumber())
      .isEqualTo(accountHolder.contactInfo().phoneNumber().value());

    assertThat(accountHolderDetails.accountHolderType())
      .isEqualTo(accountHolder.accountHolderType());

    assertThat(accountHolderDetails.createdAt())
      .isEqualTo(accountHolder.createdAt());
  }

  @Test
  void shouldReturnNull_whenAccountHolderIsNull() {
    AccountHolderDetails result = mapper.toDetails(null);
    assertThat(result).isNull();
  }
}