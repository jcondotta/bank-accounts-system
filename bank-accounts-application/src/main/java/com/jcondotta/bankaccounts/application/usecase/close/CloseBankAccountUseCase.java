package com.jcondotta.bankaccounts.application.usecase.close;

import com.jcondotta.bankaccounts.application.usecase.close.model.CloseBankAccountCommand;

public interface CloseBankAccountUseCase {

  void execute(CloseBankAccountCommand command);
}
