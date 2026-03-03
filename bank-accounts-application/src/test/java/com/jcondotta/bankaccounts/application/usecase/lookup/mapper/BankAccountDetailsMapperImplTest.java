package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.aggregates.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountDetailsMapperImplTest {

  private BankAccountDetailsMapper bankAccountMapper;

  @BeforeEach
  void setUp() {
    var accountHolderMapper = new AccountHolderDetailsMapperImpl(
      new PersonalInfoDetailsMapperImpl(new IdentityDocumentDetailsMapper() {
      }),
      new ContactInfoDetailsMapper() {
      },
      new AddressDetailsMapper() {}
    );

    bankAccountMapper = new BankAccountDetailsMapperImpl(accountHolderMapper);
  }

  @Test
  void shouldMapBankAccount_whenOnlyPrimaryHolderIsPresent() {
    BankAccount bankAccount = BankAccountTestFixture.openPendingAccount(AccountHolderFixtures.JEFFERSON);

    BankAccountDetails bankAccountDetails = bankAccountMapper.toDetails(bankAccount);

    assertThat(bankAccountDetails.id()).isEqualTo(bankAccount.getId().value());
    assertThat(bankAccountDetails.accountType()).isEqualTo(bankAccount.getAccountType());
    assertThat(bankAccountDetails.currency()).isEqualTo(bankAccount.getCurrency());
    assertThat(bankAccountDetails.iban()).isEqualTo(bankAccount.getIban().value());
    assertThat(bankAccountDetails.accountStatus()).isEqualTo(bankAccount.getAccountStatus());
    assertThat(bankAccountDetails.createdAt()).isNotNull();

    assertThat(bankAccountDetails.accountHolders())
      .extracting(AccountHolderDetails::accountHolderType)
      .containsExactly(AccountHolderType.PRIMARY);
  }

  @Test
  void shouldMapBankAccount_whenPrimaryAndJointHolderArePresent() {
    BankAccount bankAccount = BankAccountTestFixture.openActiveAccount(AccountHolderFixtures.JEFFERSON);

    bankAccount.addJointAccountHolder(
      AccountHolderFixtures.PATRIZIO.personalInfo(),
      AccountHolderFixtures.PATRIZIO.contactInfo(),
      AccountHolderFixtures.PATRIZIO.address()
    );

    BankAccountDetails details = bankAccountMapper.toDetails(bankAccount);
    assertThat(details.accountHolders())
      .hasSize(2)
      .extracting(AccountHolderDetails::accountHolderType)
      .containsExactlyInAnyOrder(AccountHolderType.PRIMARY, AccountHolderType.JOINT);
  }

  @Test
  void shouldReturnNull_whenBankAccountIsNull() {
    assertThat(bankAccountMapper.toDetails(null)).isNull();
  }
}
