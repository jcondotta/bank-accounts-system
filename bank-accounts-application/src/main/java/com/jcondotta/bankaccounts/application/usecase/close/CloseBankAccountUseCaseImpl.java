package com.jcondotta.bankaccounts.application.usecase.close;

import com.jcondotta.bankaccounts.application.usecase.close.model.CloseBankAccountCommand;
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
public class CloseBankAccountUseCaseImpl implements CloseBankAccountUseCase {

  private final BankAccountRepository bankAccountRepository;

  @Override
  @Observed(
    name = "bankAccounts.close",
    contextualName = "closeBankAccount",
    lowCardinalityKeyValues = {
      "boundedContext", "bank-accounts",
      "useCase", "close-bank-account",
      "operation", "update"
    }
  )
  public void execute(CloseBankAccountCommand command) {

    Objects.requireNonNull(command, "command must not be null");

    log.info(
      "Closing bank account [bankAccountId={}]",
      command.bankAccountId().value()
    );

    var bankAccount = bankAccountRepository.findById(command.bankAccountId())
      .orElseThrow(() -> new BankAccountNotFoundException(command.bankAccountId()));

    bankAccount.close();

    bankAccountRepository.save(bankAccount);

    log.info(
      "Bank account closed successfully [bankAccountId={}]",
      bankAccount.id().value()
    );
  }
}