package com.jcondotta.bankaccounts.application.usecase.addjointaccountholder;

import com.jcondotta.bankaccounts.application.ports.output.repository.lookupbankaccount.BankAccountLookupRepository;
import com.jcondotta.bankaccounts.application.ports.output.repository.updatebankaccount.BankAccountUpdateRepository;
import com.jcondotta.bankaccounts.application.usecase.addjointaccountholder.model.AddJointAccountHolderCommand;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddJointAccountHolderUseCaseImpl implements AddJointAccountHolderUseCase {

  private final BankAccountLookupRepository bankAccountLookupRepository;
  private final BankAccountUpdateRepository bankAccountUpdateRepository;
  private final Clock clock;

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

    log.info(
      "Adding new joint account holder [bankAccountId={}]", command.bankAccountId()
    );

    var bankAccount = bankAccountLookupRepository.byId(command.bankAccountId())
      .orElseThrow(() ->
        new BankAccountNotFoundException(command.bankAccountId())
      );

    bankAccount.addJointAccountHolder(
      command.accountHolderName(),
      command.passportNumber(),
      command.dateOfBirth(),
      ZonedDateTime.now(clock)
    );

    bankAccountUpdateRepository.save(bankAccount);

    log.info(
      "Joint account holder added successfully [bankAccountId={}]", bankAccount.getBankAccountId()
    );
  }
}
