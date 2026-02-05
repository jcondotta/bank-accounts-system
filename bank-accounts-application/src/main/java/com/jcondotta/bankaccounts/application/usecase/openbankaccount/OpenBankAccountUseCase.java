package com.jcondotta.bankaccounts.application.usecase.openbankaccount;

import com.jcondotta.bankaccounts.application.usecase.openbankaccount.model.OpenBankAccountCommand;

public interface OpenBankAccountUseCase {

  void execute(OpenBankAccountCommand command);
}
