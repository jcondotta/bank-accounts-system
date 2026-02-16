package com.jcondotta.bankaccounts.application.usecase.block;

import com.jcondotta.bankaccounts.application.usecase.block.model.BlockBankAccountCommand;

public interface BlockBankAccountUseCase {

  void execute(BlockBankAccountCommand command);
}
