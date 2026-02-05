package com.jcondotta.bankaccounts.domain.exceptions;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountNotFoundExceptionTest {

  @Test
  void shouldCreateBankAccountNotFoundException_whenBankAccountIdIsValid() {
    var bankAccountId = BankAccountId.newId();
    var exception = new BankAccountNotFoundException(bankAccountId);

    assertThat(exception)
      .isInstanceOf(DomainObjectNotFoundException.class)
      .hasMessage("Bank account not found with id: " + bankAccountId.value());
  }
}
