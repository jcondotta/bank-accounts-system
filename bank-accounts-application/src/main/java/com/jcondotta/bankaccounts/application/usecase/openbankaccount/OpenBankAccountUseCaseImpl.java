package com.jcondotta.bankaccounts.application.usecase.openbankaccount;

import com.jcondotta.bankaccounts.application.ports.output.facade.IbanGeneratorFacade;
import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountOpenedEventPublisher;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.OpenBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.openbankaccount.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.application.usecase.openbankaccount.model.OpenBankAccountResult;
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
  private final IbanGeneratorFacade ibanGeneratorFacade;
  private final BankAccountOpenedEventPublisher bankAccountOpenedEventPublisher;
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
  public OpenBankAccountResult execute(OpenBankAccountCommand command) {
    Objects.requireNonNull(command, "command must not be null");

    log.info(
      "Opening new bank account [accountType={}, currency={}]", command.accountType(), command.currency()
    );

    var iban = ibanGeneratorFacade.generate();

    BankAccount bankAccount = BankAccount.open(
      command.accountHolderName(),
      command.passportNumber(),
      command.dateOfBirth(),
      command.accountType(),
      command.currency(),
      iban,
      ZonedDateTime.now(clock)
    );

    openBankAccountRepository.create(bankAccount);

    bankAccount
      .pullDomainEvents()
      .forEach(bankAccountOpenedEventPublisher::publish);

    log.info(
      "Bank account opened successfully [bankAccountId={}]", bankAccount.getBankAccountId().value()
    );

    return new OpenBankAccountResult(bankAccount.getBankAccountId(), bankAccount.getCreatedAt());
  }
}
