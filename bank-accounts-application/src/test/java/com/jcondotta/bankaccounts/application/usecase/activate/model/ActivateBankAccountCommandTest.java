package com.jcondotta.bankaccounts.application.usecase.activate.model;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ActivateBankAccountCommandTest {

  @Test
  void shouldCreateCommand_whenBankAccountIdIsProvided() {
    var bankAccountId = BankAccountId.newId();

    var command = new ActivateBankAccountCommand(bankAccountId);

    assertThat(command.bankAccountId())
      .isEqualTo(bankAccountId);
  }

  @Test
  void shouldThrowNullPointerException_whenBankAccountIdIsNull() {
    assertThatThrownBy(() -> new ActivateBankAccountCommand(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("bankAccountId must not be null");
  }
}