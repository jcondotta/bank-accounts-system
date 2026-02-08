package com.jcondotta.bankaccounts.application.usecase.openbankaccount;

import com.jcondotta.bankaccounts.application.usecase.openbankaccount.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.application.usecase.openbankaccount.model.OpenBankAccountResult;

public interface OpenBankAccountUseCase {

  OpenBankAccountResult execute(OpenBankAccountCommand command);
}
