package com.jcondotta.bankaccounts.application.usecase.addholder;

import com.jcondotta.bankaccounts.application.usecase.addholder.model.AddJointAccountHolderCommand;

public interface AddJointAccountHolderUseCase {

  void execute(AddJointAccountHolderCommand command);
}
