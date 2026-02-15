package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.unblockbankaccount;

import com.jcondotta.bankaccounts.application.usecase.unblockbankaccount.UnblockBankAccountUseCase;
import com.jcondotta.bankaccounts.application.usecase.unblockbankaccount.model.UnblockBankAccountCommand;
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
public class UnblockBankAccountControllerImpl implements UnblockBankAccountController {

  private final UnblockBankAccountUseCase useCase;

  @Override
  public ResponseEntity<Void> unblock(UUID bankAccountId) {
    useCase.execute(new UnblockBankAccountCommand(BankAccountId.of(bankAccountId)));
    return ResponseEntity.noContent().build();
  }
}