package com.jcondotta.bankaccounts.application.ports.output.facade;

import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.Iban;

public interface IbanGeneratorFacade {

  Iban generate();
}
