package com.jcondotta.bankaccounts.application.usecase.unblock;

import com.jcondotta.bankaccounts.application.usecase.unblock.model.UnblockBankAccountCommand;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.repository.BankAccountRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnblockBankAccountUseCaseImpl implements UnblockBankAccountUseCase {

  private final BankAccountRepository bankAccountRepository;

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

    var bankAccount = bankAccountRepository.findById(command.bankAccountId())
        .orElseThrow(() -> new BankAccountNotFoundException(command.bankAccountId()));

    bankAccount.unblock();

    bankAccountRepository.save(bankAccount);

    log.info(
      "Bank account unblocked successfully [bankAccountId={}]",
      bankAccount.id().value()
    );
  }
}
