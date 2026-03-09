package com.jcondotta.bankaccounts.application.usecase.close.model;

import com.jcondotta.banking.accounts.domain.bankaccount.identity.BankAccountId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CloseBankAccountCommandTest {
  @Test
  void shouldCreateCommandWithBankAccountId() {
    var bankAccountId = BankAccountId.newId();

    var command = new CloseBankAccountCommand(bankAccountId);

    assertThat(command.bankAccountId()).isEqualTo(bankAccountId);
  }

  @Test
  void shouldThrowNullPointerException_whenBankAccountIdIsNull() {
    assertThatThrownBy(() -> new CloseBankAccountCommand(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("id must not be null");
  }
}