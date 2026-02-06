package com.jcondotta.bankaccounts.infrastructure.adapters;

import com.jcondotta.bankaccounts.application.ports.output.IbanGenerator;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import org.springframework.stereotype.Component;

@Component
public class Iban implements IbanGenerator {
  @Override
  public com.jcondotta.bankaccounts.domain.value_objects.Iban generate(AccountType accountType, Currency currency) {
    return com.jcondotta.bankaccounts.domain.value_objects.Iban.of("DE44500105175407324931");
  }
}
