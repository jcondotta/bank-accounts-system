package com.jcondotta.bankaccounts.application.usecase.closebankaccount.model;

import com.jcondotta.bankaccounts.application.usecase.unblockbankaccount.model.UnblockBankAccountCommand;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
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
      .hasMessage("bankAccountId must not be null");
  }
}