package com.jcondotta.bankaccounts.infrastructure.adapters;

import com.jcondotta.bankaccounts.application.ports.output.facade.IbanGeneratorFacade;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import org.springframework.stereotype.Component;

@Component
public class IbanGenerator implements IbanGeneratorFacade {

  @Override
  public Iban generate() {
    return Iban.of("DE44500105175407324931");
  }
}
