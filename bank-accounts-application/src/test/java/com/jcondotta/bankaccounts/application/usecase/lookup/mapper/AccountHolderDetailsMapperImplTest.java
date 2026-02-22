package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.AccountHolderDetails;
import com.jcondotta.bankaccounts.domain.entities.AccountHolder;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountHolderDetailsMapperImplTest {

  private static final AccountHolderDetailsMapper mapper = new AccountHolderDetailsMapperImpl();

  private static final Iban VALID_IBAN = Iban.of("ES3801283316232166447417");

  @Test
  void shouldMapAccountHolderToAccountHolderDetails_whenAllFieldsArePresent() {

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

    AccountHolder primary = bankAccount.primaryAccountHolder();
    AccountHolder joint = bankAccount.jointAccountHolders().getFirst();

    assertMapping(primary, mapper.toDetails(primary));
    assertMapping(joint, mapper.toDetails(joint));
  }

  private void assertMapping(AccountHolder source, AccountHolderDetails target) {
    assertThat(target.id()).isEqualTo(source.id());
    assertThat(target.name()).isEqualTo(source.name());
    assertThat(target.passportNumber()).isEqualTo(source.passportNumber());
    assertThat(target.dateOfBirth()).isEqualTo(source.dateOfBirth());
    assertThat(target.email()).isEqualTo(source.email());
    assertThat(target.accountHolderType()).isEqualTo(source.accountHolderType());
    assertThat(target.createdAt()).isNotNull();
  }

  @Test
  void shouldReturnNull_whenAccountHolderIsNull() {
    AccountHolderDetails details = mapper.toDetails(null);
    assertThat(details).isNull();
  }
}
