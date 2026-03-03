package com.jcondotta.bankaccounts.application.command.open;

import com.jcondotta.bankaccounts.application.command.open.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.application.command.open.model.OpenBankAccountResult;

public interface OpenBankAccountUseCase {

  OpenBankAccountResult execute(OpenBankAccountCommand command);
}
