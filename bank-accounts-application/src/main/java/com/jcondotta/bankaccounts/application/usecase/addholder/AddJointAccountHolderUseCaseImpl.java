package com.jcondotta.bankaccounts.application.usecase.addholder;

import com.jcondotta.bankaccounts.application.usecase.addholder.model.AddJointAccountHolderCommand;
import com.jcondotta.bankaccounts.domain.enums.DocumentType;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.repository.BankAccountRepository;
import com.jcondotta.bankaccounts.domain.value_objects.personal.DocumentNumber;
import com.jcondotta.bankaccounts.domain.value_objects.personal.IdentityDocument;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddJointAccountHolderUseCaseImpl implements AddJointAccountHolderUseCase {

  private final BankAccountRepository bankAccountRepository;

  @Override
  @Observed(
    name = "bankAccounts.addJointAccountHolder",
    contextualName = "addJointAccountHolder",
    lowCardinalityKeyValues = {
      "boundedContext", "bank-accounts",
      "useCase", "add-joint-account-holder",
      "operation", "update"
    }
  )
  public void execute(AddJointAccountHolderCommand command) {
    Objects.requireNonNull(command, "command must not be null");

    var bankAccount = bankAccountRepository.findById(command.bankAccountId())
      .orElseThrow(() -> new BankAccountNotFoundException(command.bankAccountId()));

    bankAccount.addJointAccountHolder(
      command.personalInfo(),
      command.contactInfo(),
      command.address()
    );

    bankAccountRepository.save(bankAccount);

    log.info(
      "Joint account holder added successfully [bankAccountId={}]", bankAccount.id().value()
    );
  }
}
