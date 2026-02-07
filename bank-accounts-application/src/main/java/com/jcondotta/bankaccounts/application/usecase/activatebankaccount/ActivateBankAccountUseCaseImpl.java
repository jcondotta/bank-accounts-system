package com.jcondotta.bankaccounts.application.usecase.activatebankaccount;

import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountActivatedEventPublisher;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.LookupBankAccountRepository;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.UpdateBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.activatebankaccount.model.ActivateBankAccountCommand;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivateBankAccountUseCaseImpl implements ActivateBankAccountUseCase {

  private final LookupBankAccountRepository lookupBankAccountRepository;
  private final UpdateBankAccountRepository updateBankAccountRepository;
  private final BankAccountActivatedEventPublisher bankAccountActivatedEventPublisher;

  @Override
  @Observed(
    name = "bankAccounts.activate",
    contextualName = "activateBankAccount",
    lowCardinalityKeyValues = {
      "boundedContext", "bank-accounts",
      "useCase", "activate-bank-account",
      "operation", "update"
    }
  )
  public void execute(ActivateBankAccountCommand command) {
    Objects.requireNonNull(command, "command must not be null");

    log.info("Activating bank account [bankAccountId={}]", command.bankAccountId().value());

    var bankAccount = lookupBankAccountRepository.byId(command.bankAccountId())
      .orElseThrow(() -> new BankAccountNotFoundException(command.bankAccountId()));

    bankAccount.activate();
    updateBankAccountRepository.update(bankAccount);

    bankAccount
      .pullDomainEvents()
      .forEach(bankAccountActivatedEventPublisher::publish);

    log.info(
      "Bank account activated successfully [bankAccountId={}]", bankAccount.getBankAccountId().value()
    );
  }
}
