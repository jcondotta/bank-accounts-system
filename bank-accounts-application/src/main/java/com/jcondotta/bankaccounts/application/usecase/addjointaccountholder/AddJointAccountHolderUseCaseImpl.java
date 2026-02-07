package com.jcondotta.bankaccounts.application.usecase.addjointaccountholder;

import com.jcondotta.bankaccounts.application.ports.output.messaging.JointAccountHolderAddedEventPublisher;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.LookupBankAccountRepository;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.UpdateBankAccountRepository;
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

  private final LookupBankAccountRepository lookupBankAccountRepository;
  private final UpdateBankAccountRepository updateBankAccountRepository;
  private final JointAccountHolderAddedEventPublisher jointAccountHolderAddedEventPublisher;
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

    var bankAccount = lookupBankAccountRepository.byId(command.bankAccountId())
      .orElseThrow(() -> new BankAccountNotFoundException(command.bankAccountId()));

    bankAccount.addJointAccountHolder(
      command.accountHolderName(),
      command.passportNumber(),
      command.dateOfBirth(),
      ZonedDateTime.now(clock)
    );

    updateBankAccountRepository.update(bankAccount);

    bankAccount
      .pullDomainEvents()
      .forEach(jointAccountHolderAddedEventPublisher::publish);

    log.info(
      "Joint account holder added successfully [bankAccountId={}]", bankAccount.getBankAccountId()
    );
  }
}
