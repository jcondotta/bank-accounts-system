package com.jcondotta.bankaccounts.application.usecase.activate;

import com.jcondotta.bankaccounts.application.usecase.activate.model.ActivateBankAccountCommand;

public interface ActivateBankAccountUseCase {

  void execute(ActivateBankAccountCommand command);
}
