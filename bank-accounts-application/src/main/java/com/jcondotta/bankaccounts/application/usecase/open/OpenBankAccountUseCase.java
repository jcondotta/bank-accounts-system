package com.jcondotta.bankaccounts.application.usecase.open;

import com.jcondotta.bankaccounts.application.usecase.open.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.application.usecase.open.model.OpenBankAccountResult;

public interface OpenBankAccountUseCase {

  OpenBankAccountResult execute(OpenBankAccountCommand command);
}
