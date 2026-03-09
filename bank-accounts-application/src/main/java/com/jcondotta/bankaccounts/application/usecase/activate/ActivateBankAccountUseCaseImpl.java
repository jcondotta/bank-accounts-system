package com.jcondotta.bankaccounts.application.usecase.activate;

import com.jcondotta.bankaccounts.application.usecase.activate.model.ActivateBankAccountCommand;
import com.jcondotta.banking.accounts.domain.bankaccount.exceptions.BankAccountNotFoundException;
import com.jcondotta.banking.accounts.domain.bankaccount.repository.BankAccountRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivateBankAccountUseCaseImpl implements ActivateBankAccountUseCase {

  private final BankAccountRepository bankAccountRepository;

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

    log.info("Activating bank account [id={}]", command.bankAccountId().value());

    var bankAccount = bankAccountRepository.findById(command.bankAccountId())
      .orElseThrow(() -> new BankAccountNotFoundException(command.bankAccountId()));

    bankAccount.activate();
    bankAccountRepository.save(bankAccount);

    log.info(
      "Bank account activated successfully [id={}]", bankAccount.getId().value()
    );
  }
}
