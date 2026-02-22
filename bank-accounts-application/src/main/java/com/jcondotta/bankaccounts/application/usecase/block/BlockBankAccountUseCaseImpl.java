package com.jcondotta.bankaccounts.application.usecase.block;

import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountBlockedEventPublisher;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.LookupBankAccountRepository;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.UpdateBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.block.model.BlockBankAccountCommand;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlockBankAccountUseCaseImpl implements BlockBankAccountUseCase {

  private final LookupBankAccountRepository lookupBankAccountRepository;
  private final UpdateBankAccountRepository updateBankAccountRepository;
  private final BankAccountBlockedEventPublisher bankAccountBlockedEventPublisher;

  @Override
  @Observed(
    name = "bankAccounts.block",
    contextualName = "blockBankAccount",
    lowCardinalityKeyValues = {
      "boundedContext", "bank-accounts",
      "useCase", "block-bank-account",
      "operation", "update"
    }
  )
  public void execute(BlockBankAccountCommand command) {
    Objects.requireNonNull(command, "command must not be null");

    log.info(
      "Blocking bank account [bankAccountId={}]", command.bankAccountId().value()
    );

    var bankAccount = lookupBankAccountRepository.byId(command.bankAccountId())
        .orElseThrow(() -> new BankAccountNotFoundException(command.bankAccountId()));

    bankAccount.block();

    updateBankAccountRepository.update(bankAccount);

//    bankAccount
//      .pullEvents()
//      .forEach(bankAccountBlockedEventPublisher::publish);

    log.info(
      "Bank account blocked successfully [bankAccountId={}]", bankAccount.id().value()
    );
  }
}