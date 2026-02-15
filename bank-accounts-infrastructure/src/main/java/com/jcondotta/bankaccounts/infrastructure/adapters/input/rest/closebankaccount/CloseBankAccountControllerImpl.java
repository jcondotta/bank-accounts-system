package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.closebankaccount;

import com.jcondotta.bankaccounts.application.usecase.closebankaccount.CloseBankAccountUseCase;
import com.jcondotta.bankaccounts.application.usecase.closebankaccount.model.CloseBankAccountCommand;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
public class CloseBankAccountControllerImpl implements CloseBankAccountController {

  private final CloseBankAccountUseCase useCase;

  @Override
  public ResponseEntity<Void> close(UUID bankAccountId) {
    useCase.execute(new CloseBankAccountCommand(BankAccountId.of(bankAccountId)));
    return ResponseEntity.noContent().build();
  }
}