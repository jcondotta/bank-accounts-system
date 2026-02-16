package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.activatebankaccount;

import com.jcondotta.bankaccounts.application.usecase.activate.ActivateBankAccountUseCase;
import com.jcondotta.bankaccounts.application.usecase.activate.model.ActivateBankAccountCommand;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
public class ActivateBankAccountControllerImpl implements ActivateBankAccountController {

  private final ActivateBankAccountUseCase activateBankAccountUseCase;

  @Override
  public ResponseEntity<Void> activateBankAccount(@PathVariable("bank-account-id") UUID bankAccountId) {
    activateBankAccountUseCase.execute(new ActivateBankAccountCommand(BankAccountId.of(bankAccountId)));
    return ResponseEntity.noContent().build();
  }
}