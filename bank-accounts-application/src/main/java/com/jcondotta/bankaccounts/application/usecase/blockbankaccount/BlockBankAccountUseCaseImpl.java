package com.jcondotta.bankaccounts.application.usecase.blockbankaccount;

import com.jcondotta.bankaccounts.application.ports.output.messaging.DomainEventPublisher;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.lookupbankaccount.BankAccountLookupRepository;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.updatebankaccount.BankAccountUpdateRepository;
import com.jcondotta.bankaccounts.application.usecase.blockbankaccount.model.BlockBankAccountCommand;
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

  private final BankAccountLookupRepository bankAccountLookupRepository;
  private final BankAccountUpdateRepository bankAccountUpdateRepository;
  private final DomainEventPublisher domainEventPublisher;

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

    var bankAccount = bankAccountLookupRepository.byId(command.bankAccountId())
        .orElseThrow(() -> new BankAccountNotFoundException(command.bankAccountId()));

    bankAccount.block();

    bankAccountUpdateRepository.save(bankAccount);

    bankAccount
      .pullDomainEvents()
      .forEach(domainEventPublisher::publish);

    log.info(
      "Bank account blocked successfully [bankAccountId={}]", bankAccount.getBankAccountId().value()
    );
  }
}