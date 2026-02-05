package com.jcondotta.bankaccounts.application.usecase.openbankaccount;

import com.jcondotta.bankaccounts.application.ports.output.IbanGenerator;
import com.jcondotta.bankaccounts.application.ports.output.repository.openbankaccount.OpenBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.openbankaccount.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
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
public class OpenBankAccountUseCaseImpl implements OpenBankAccountUseCase {

  private final OpenBankAccountRepository openBankAccountRepository;
  private final IbanGenerator ibanGenerator;
  private final Clock clock;

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
  public void execute(OpenBankAccountCommand command) {
    Objects.requireNonNull(command, "command must not be null");

    log.info(
      "Opening new bank account [accountType={}, currency={}]", command.accountType(), command.currency()
    );

    var iban = ibanGenerator.generate(command.accountType(), command.currency());

    BankAccount bankAccount = BankAccount.open(
      command.accountHolderName(),
      command.passportNumber(),
      command.dateOfBirth(),
      command.accountType(),
      command.currency(),
      iban,
      ZonedDateTime.now(clock)
    );

    openBankAccountRepository.save(bankAccount);

    log.info(
      "Bank account opened successfully [bankAccountId={}]", bankAccount.getBankAccountId()
    );
  }
}
