package com.jcondotta.bankaccounts.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyTest {

  @Test
  void shouldIdentifyEuro_whenCurrencyIsEuros() {
    assertThat(Currency.EUR.isEuro()).isTrue();
    assertThat(Currency.EUR.description()).isEqualTo("Euro");

    assertThat(Currency.USD.isEuro()).isFalse();
  }

  @Test
  void shouldIdentifyUsd_whenCurrencyIsDollars() {
    assertThat(Currency.USD.isUSDollar()).isTrue();
    assertThat(Currency.USD.description()).isEqualTo("US Dollar");

    assertThat(Currency.EUR.isUSDollar()).isFalse();
  }
}
