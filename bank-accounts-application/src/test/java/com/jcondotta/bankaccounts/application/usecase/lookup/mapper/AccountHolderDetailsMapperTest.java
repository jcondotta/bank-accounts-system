package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.domain.aggregates.AccountHolder;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class AccountHolderDetailsMapperTest {

  private final AccountHolderDetailsMapper mapper = new AccountHolderDetailsMapperImpl(
    Mappers.getMapper(PersonalInfoDetailsMapper.class),
    Mappers.getMapper(ContactInfoDetailsMapper.class),
    Mappers.getMapper(AddressDetailsMapper.class)
  );

  @Test
  void shouldMapAccountHolderToDetails_whenAllFieldsArePresent() {
    var accountHolder = BankAccountTestFixture.createPrimaryHolder(AccountHolderFixtures.JEFFERSON, Instant.now());

    AccountHolderDetails details = mapper.toDetails(accountHolder);

    assertThat(details)
      .satisfies(holderDetails -> {
        assertThat(holderDetails.id()).isEqualTo(accountHolder.getId().value());
        assertThat(holderDetails.accountHolderType()).isEqualTo(accountHolder.getAccountHolderType());
        assertThat(holderDetails.createdAt()).isEqualTo(accountHolder.getCreatedAt());

        assertPersonalInfo(holderDetails, accountHolder);
        assertContactInfo(holderDetails, accountHolder);
        assertAddress(holderDetails, accountHolder);
      });
  }

  @Test
  void shouldReturnNull_whenAccountHolderIsNull() {
    assertThat(mapper.toDetails(null)).isNull();
  }

  private void assertPersonalInfo(AccountHolderDetails details, AccountHolder accountHolder) {
    var personalInfo = details.personalInfo();
    var source = accountHolder.getPersonalInfo();

    assertThat(personalInfo.firstName()).isEqualTo(source.holderName().firstName());
    assertThat(personalInfo.lastName()).isEqualTo(source.holderName().lastName());
    assertThat(personalInfo.dateOfBirth()).isEqualTo(source.dateOfBirth().value());

    assertThat(personalInfo.identityDocument().country())
      .isEqualTo(source.identityDocument().country().name());
    assertThat(personalInfo.identityDocument().type())
      .isEqualTo(source.identityDocument().type().name());
    assertThat(personalInfo.identityDocument().number())
      .isEqualTo(source.identityDocument().number().value());
  }

  private void assertContactInfo(AccountHolderDetails details, AccountHolder accountHolder) {
    var contactInfo = details.contactInfo();
    var source = accountHolder.getContactInfo();

    assertThat(contactInfo.email()).isEqualTo(source.email().value());
    assertThat(contactInfo.phoneNumber()).isEqualTo(source.phoneNumber().value());
  }

  private void assertAddress(AccountHolderDetails details, AccountHolder accountHolder) {
    var address = details.address();
    var source = accountHolder.getAddress();

    assertThat(address.street()).isEqualTo(source.street().value());
    assertThat(address.streetNumber()).isEqualTo(source.streetNumber().value());
    assertThat(address.addressComplement()).isEqualTo(source.complement().value());
    assertThat(address.postalCode()).isEqualTo(source.postalCode().value());
    assertThat(address.city()).isEqualTo(source.city().value());
  }
}