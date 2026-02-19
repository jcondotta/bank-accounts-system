package com.jcondotta.bankaccounts.domain.exceptions;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountNotActiveExceptionTest {

  @ParameterizedTest
  @EnumSource(value = AccountStatus.class, names = "ACTIVE", mode = EnumSource.Mode.EXCLUDE)
  void shouldCreateBankAccountNotActiveException_whenStatusIsNotActive(AccountStatus accountStatus) {
    var exception = new BankAccountNotActiveException(accountStatus);

    assertThat(exception)
//      .isInstanceOf(DomainException.class)
      .isInstanceOf(DomainRuleValidationException.class)
      .hasMessage("Bank account must be ACTIVE to perform this operation. Current status: " + accountStatus);
  }
}