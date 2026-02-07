package com.jcondotta.bankaccounts.application.ports.output.facade;

import com.jcondotta.bankaccounts.domain.value_objects.Iban;

public interface IbanGeneratorFacade {

  Iban generate();
}
