package com.jcondotta.bankaccounts.application.usecase.open;

import com.jcondotta.bankaccounts.application.ports.output.facade.IbanGeneratorFacade;
import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountOpenedEventPublisher;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.OpenBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.open.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.application.usecase.open.model.OpenBankAccountResult;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenBankAccountUseCaseImpl implements OpenBankAccountUseCase {

  private final OpenBankAccountRepository openBankAccountRepository;
  private final IbanGeneratorFacade ibanGeneratorFacade;
  private final BankAccountOpenedEventPublisher bankAccountOpenedEventPublisher;

  @Override
  @Observed(
    name = "bankAccounts.open",
    contextualName = "openBankAccount",
    lowCardinalityKeyValues = {
      "boundedContext", "bank-accounts",
      "useCase", "open-bank-account",
      "operation", "create"
    }
  )
  public OpenBankAccountResult execute(OpenBankAccountCommand command) {
    Objects.requireNonNull(command, "command must not be null");

    var iban = ibanGeneratorFacade.generate();

    BankAccount bankAccount = BankAccount.open(
      command.name(),
      command.passportNumber(),
      command.dateOfBirth(),
      command.email(),
      command.accountType(),
      command.currency(),
      iban
    );

    openBankAccountRepository.create(bankAccount);

    log.atInfo()
      .setMessage("Bank account created successfully.")
      .addKeyValue("bankAccountId", bankAccount.id())
      .log();

    return new OpenBankAccountResult(bankAccount.id(), bankAccount.createdAt());
  }
}
