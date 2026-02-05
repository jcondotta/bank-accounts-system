package com.jcondotta.bankaccounts.application.usecase.addjointaccountholder;

import com.jcondotta.bankaccounts.application.usecase.addjointaccountholder.model.AddJointAccountHolderCommand;

public interface AddJointAccountHolderUseCase {

  void execute(AddJointAccountHolderCommand command);
}
