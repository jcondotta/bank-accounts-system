package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.arguments_provider.BlankValuesArgumentProvider;
import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountHolderNameTest {

  private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = "Jefferson Condotta";
  private static final String ACCOUNT_HOLDER_NAME_PATRIZIO = "Patrizio Condotta";

  @Test
  void shouldCreateAccountHolderName_whenValueIsValid() {
    var name = AccountHolderName.of(ACCOUNT_HOLDER_NAME_JEFFERSON);

    assertThat(name)
      .extracting(AccountHolderName::value)
      .isEqualTo(ACCOUNT_HOLDER_NAME_JEFFERSON);
  }

  @Test
  void shouldThrowDomainValidationException_whenNameIsNull() {
    assertThatThrownBy(() -> AccountHolderName.of(null))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolderName.ACCOUNT_HOLDER_NAME_NOT_NULL);
  }

  @ParameterizedTest
  @ArgumentsSource(BlankValuesArgumentProvider.class)
  void shouldThrowDomainValidationException_whenNameIsBlank(String blankValue) {
    assertThatThrownBy(() -> AccountHolderName.of(blankValue))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolderName.ACCOUNT_HOLDER_NAME_NOT_BLANK);
  }

  @Test
  void shouldThrowDomainValidationException_whenNameIsTooLong() {
    var longName = "A".repeat(AccountHolderName.MAX_LENGTH + 1);

    assertThatThrownBy(() -> AccountHolderName.of(longName))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(AccountHolderName.ACCOUNT_HOLDER_NAME_TOO_LONG);
  }

  @Test
  void shouldNormalizeWhitespace() {
    var name = AccountHolderName.of("  Jefferson   Condotta  ");
    assertThat(name.value()).isEqualTo(ACCOUNT_HOLDER_NAME_JEFFERSON);
  }

  @Test
  void shouldBeEqual_whenNamesHaveSameValue() {
    var name1 = AccountHolderName.of(ACCOUNT_HOLDER_NAME_JEFFERSON);
    var name2 = AccountHolderName.of(ACCOUNT_HOLDER_NAME_JEFFERSON);

    assertThat(name1)
      .isEqualTo(name2)
      .hasSameHashCodeAs(name2);
  }

  @Test
  void shouldNotBeEqual_whenNamesHaveDifferentValues() {
    var name1 = AccountHolderName.of(ACCOUNT_HOLDER_NAME_JEFFERSON);
    var name2 = AccountHolderName.of(ACCOUNT_HOLDER_NAME_PATRIZIO);

    assertThat(name1).isNotEqualTo(name2);
  }
}