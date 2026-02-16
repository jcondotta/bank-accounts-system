package com.jcondotta.bankaccounts.application.usecase.unblock;

import com.jcondotta.bankaccounts.application.usecase.unblock.model.UnblockBankAccountCommand;

public interface UnblockBankAccountUseCase {

  void execute(UnblockBankAccountCommand command);
}
