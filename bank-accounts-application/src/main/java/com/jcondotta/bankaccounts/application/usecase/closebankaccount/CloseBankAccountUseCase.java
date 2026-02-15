package com.jcondotta.bankaccounts.application.usecase.closebankaccount;

import com.jcondotta.bankaccounts.application.usecase.closebankaccount.model.CloseBankAccountCommand;

public interface CloseBankAccountUseCase {

  void execute(CloseBankAccountCommand command);
}
