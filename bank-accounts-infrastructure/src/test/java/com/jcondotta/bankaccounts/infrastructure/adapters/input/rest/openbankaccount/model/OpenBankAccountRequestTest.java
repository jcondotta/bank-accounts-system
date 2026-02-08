package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model;

import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.infrastructure.arguments_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.infrastructure.config.ValidatorTestFactory;
import com.jcondotta.bankaccounts.infrastructure.fixtures.AccountHolderFixtures;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class OpenBankAccountRequestTest {

  private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

  private static final PrimaryAccountHolderRequest VALID_ACCOUNT_HOLDER =
    new PrimaryAccountHolderRequest(
      AccountHolderFixtures.JEFFERSON.getAccountHolderName().value(),
      AccountHolderFixtures.JEFFERSON.getDateOfBirth().value(),
      AccountHolderFixtures.JEFFERSON.getPassportNumber().value()
    );

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldNotDetectConstraintViolation_whenRequestIsValid(
    AccountType accountType,
    Currency currency
  ) {
    var request = new OpenBankAccountRequest(
      accountType,
      currency,
      VALID_ACCOUNT_HOLDER
    );

    assertThat(VALIDATOR.validate(request)).isEmpty();
  }

  @ParameterizedTest
  @EnumSource(Currency.class)
  void shouldDetectConstraintViolation_whenAccountTypeIsNull(Currency currency) {
    var request = new OpenBankAccountRequest(
      null,
      currency,
      VALID_ACCOUNT_HOLDER
    );

    assertThat(VALIDATOR.validate(request))
      .hasSize(1)
      .first()
      .satisfies(violation ->
        assertThat(violation.getPropertyPath()).hasToString("accountType")
      );
  }

  @ParameterizedTest
  @EnumSource(AccountType.class)
  void shouldDetectConstraintViolation_whenCurrencyIsNull(AccountType accountType) {
    var request = new OpenBankAccountRequest(
      accountType,
      null,
      VALID_ACCOUNT_HOLDER
    );

    assertThat(VALIDATOR.validate(request))
      .hasSize(1)
      .first()
      .satisfies(violation ->
        assertThat(violation.getPropertyPath()).hasToString("currency")
      );
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldDetectConstraintViolation_whenAccountHolderIsNull(
    AccountType accountType,
    Currency currency
  ) {
    var request = new OpenBankAccountRequest(
      accountType,
      currency,
      null
    );

    assertThat(VALIDATOR.validate(request))
      .hasSize(1)
      .first()
      .satisfies(violation ->
        assertThat(violation.getPropertyPath()).hasToString("accountHolder")
      );
  }

  @Test
  void shouldDetectConstraintViolation_whenAccountHolderIsInvalid() {
    var invalidAccountHolder = new PrimaryAccountHolderRequest(
      null,
      null,
      null
    );

    var request = new OpenBankAccountRequest(
      AccountType.SAVINGS,
      Currency.USD,
      invalidAccountHolder
    );

    assertThat(VALIDATOR.validate(request)).isNotEmpty();
  }
}
