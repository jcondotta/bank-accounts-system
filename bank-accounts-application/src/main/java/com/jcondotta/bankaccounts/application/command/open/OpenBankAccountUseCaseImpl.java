package com.jcondotta.bankaccounts.application.command.open;

import com.jcondotta.bankaccounts.application.command.open.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.application.command.open.model.OpenBankAccountResult;
import com.jcondotta.bankaccounts.application.ports.output.facade.IbanGeneratorFacade;
import com.jcondotta.banking.accounts.domain.bankaccount.aggregate.BankAccount;
import com.jcondotta.banking.accounts.domain.bankaccount.repository.BankAccountRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenBankAccountUseCaseImpl implements OpenBankAccountUseCase {

  private final BankAccountRepository bankAccountRepository;
  private final IbanGeneratorFacade ibanGeneratorFacade;

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
      command.personalInfo(),
      command.contactInfo(),
      command.address(),
      command.accountType(),
      command.currency(),
      iban
    );

    bankAccountRepository.save(bankAccount);

    log.atInfo()
      .setMessage("Bank account created successfully.")
      .addKeyValue("id", bankAccount.getId())
      .log();

    return new OpenBankAccountResult(bankAccount.getId(), bankAccount.getCreatedAt());
  }
}
