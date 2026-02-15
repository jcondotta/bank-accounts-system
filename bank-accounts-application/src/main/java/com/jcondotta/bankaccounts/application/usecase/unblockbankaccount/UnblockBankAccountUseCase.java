package com.jcondotta.bankaccounts.application.usecase.unblockbankaccount;

import com.jcondotta.bankaccounts.application.usecase.unblockbankaccount.model.UnblockBankAccountCommand;

public interface UnblockBankAccountUseCase {

  void execute(UnblockBankAccountCommand command);
}
