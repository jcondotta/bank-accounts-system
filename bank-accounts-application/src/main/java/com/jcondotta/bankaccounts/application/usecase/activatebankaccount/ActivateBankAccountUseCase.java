package com.jcondotta.bankaccounts.application.usecase.activatebankaccount;

import com.jcondotta.bankaccounts.application.usecase.activatebankaccount.model.ActivateBankAccountCommand;

public interface ActivateBankAccountUseCase {

  void execute(ActivateBankAccountCommand command);
}
