package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.entities.AccountHolder;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountDetailsMapperImplTest {

  private static final BankAccountDetailsMapper mapper = new BankAccountDetailsMapperImpl(new AccountHolderDetailsMapperImpl());

  private static final Iban VALID_IBAN =
    Iban.of("ES3801283316232166447417");

  @Test
  void shouldMapBankAccount_whenOnlyPrimaryHolderIsPresent() {

    BankAccount bankAccount = BankAccount.open(
      AccountHolderFixtures.JEFFERSON.getAccountHolderName(),
      AccountHolderFixtures.JEFFERSON.getPassportNumber(),
      AccountHolderFixtures.JEFFERSON.getDateOfBirth(),
      AccountHolderFixtures.JEFFERSON.getEmail(),
      AccountType.CHECKING,
      Currency.EUR,
      VALID_IBAN
    );

    BankAccountDetails details = mapper.toDetails(bankAccount);

    assertThat(details.bankAccountId()).isEqualTo(bankAccount.id());
    assertThat(details.accountType()).isEqualTo(bankAccount.accountType());
    assertThat(details.currency()).isEqualTo(bankAccount.currency());
    assertThat(details.iban()).isEqualTo(bankAccount.iban());
    assertThat(details.accountStatus()).isEqualTo(bankAccount.accountStatus());
    assertThat(details.createdAt()).isNotNull();

    assertThat(details.accountHolders())
      .hasSize(1)
      .singleElement()
      .satisfies(accountHolderDetails -> {
        assertMapping(bankAccount.primaryAccountHolder(), accountHolderDetails);
      });
  }

  @Test
  void shouldMapBankAccount_whenPrimaryAndJointHolderArePresent() {
    BankAccount bankAccount = BankAccount.open(
      AccountHolderFixtures.JEFFERSON.getAccountHolderName(),
      AccountHolderFixtures.JEFFERSON.getPassportNumber(),
      AccountHolderFixtures.JEFFERSON.getDateOfBirth(),
      AccountHolderFixtures.JEFFERSON.getEmail(),
      AccountType.CHECKING,
      Currency.EUR,
      VALID_IBAN
    );

    bankAccount.activate();

    bankAccount.addJointAccountHolder(
      AccountHolderFixtures.PATRIZIO.getAccountHolderName(),
      AccountHolderFixtures.PATRIZIO.getPassportNumber(),
      AccountHolderFixtures.PATRIZIO.getDateOfBirth(),
      AccountHolderFixtures.PATRIZIO.getEmail()
    );

    BankAccountDetails details = mapper.toDetails(bankAccount);
    assertThat(details.accountHolders()).hasSize(2);

    assertThat(details.accountHolders())
      .filteredOn(holder -> holder.accountHolderType() == AccountHolderType.PRIMARY)
      .singleElement()
      .satisfies(primaryHolder -> assertMapping(bankAccount.primaryAccountHolder(), primaryHolder));

    assertThat(details.accountHolders())
      .filteredOn(holder -> holder.accountHolderType() == AccountHolderType.JOINT)
      .singleElement()
      .satisfies(jointHolder -> assertMapping(bankAccount.jointAccountHolders().getFirst(), jointHolder));
  }

  private void assertMapping(AccountHolder source, AccountHolderDetails target) {
    assertThat(target)
      .usingRecursiveComparison()
      .isEqualTo(new AccountHolderDetails(
        source.id(),
        source.name(),
        source.passportNumber(),
        source.dateOfBirth(),
        source.email(),
        source.accountHolderType(),
        source.createdAt()
      ));
  }

  @Test
  void shouldReturnNull_whenBankAccountIsNull() {
    assertThat(mapper.toDetails(null)).isNull();
  }
}
