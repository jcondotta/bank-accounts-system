package com.jcondotta.bankaccounts.application.usecase.blockbankaccount;

import com.jcondotta.bankaccounts.application.usecase.blockbankaccount.model.BlockBankAccountCommand;

public interface BlockBankAccountUseCase {

  void execute(BlockBankAccountCommand command);
}
