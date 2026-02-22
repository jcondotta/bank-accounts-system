package com.jcondotta.bankaccounts.application.usecase.unblock;

import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountUnblockedEventPublisher;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.LookupBankAccountRepository;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.UpdateBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.unblock.model.UnblockBankAccountCommand;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnblockBankAccountUseCaseImpl implements UnblockBankAccountUseCase {

  private final LookupBankAccountRepository lookupBankAccountRepository;
  private final UpdateBankAccountRepository updateBankAccountRepository;
  private final BankAccountUnblockedEventPublisher bankAccountUnblockedEventPublisher;

  @Override
  @Observed(
    name = "bankAccounts.unblock",
    contextualName = "unblockBankAccount",
    lowCardinalityKeyValues = {
      "boundedContext", "bank-accounts",
      "useCase", "unblock-bank-account",
      "operation", "update"
    }
  )
  public void execute(UnblockBankAccountCommand command) {

    Objects.requireNonNull(command, "command must not be null");

    log.info(
      "Unblocking bank account [bankAccountId={}]",
      command.bankAccountId().value()
    );

    var bankAccount = lookupBankAccountRepository.byId(command.bankAccountId())
        .orElseThrow(() -> new BankAccountNotFoundException(command.bankAccountId()));

    bankAccount.unblock();

    updateBankAccountRepository.update(bankAccount);

//    bankAccount
//        .pullEvents()
//        .forEach(bankAccountUnblockedEventPublisher::publish);

    log.info(
      "Bank account unblocked successfully [bankAccountId={}]",
      bankAccount.id().value()
    );
  }
}
