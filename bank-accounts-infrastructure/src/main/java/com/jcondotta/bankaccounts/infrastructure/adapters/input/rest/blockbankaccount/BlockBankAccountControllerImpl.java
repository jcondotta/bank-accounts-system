package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.blockbankaccount;

import com.jcondotta.bankaccounts.application.usecase.block.BlockBankAccountUseCase;
import com.jcondotta.bankaccounts.application.usecase.block.model.BlockBankAccountCommand;
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
public class BlockBankAccountControllerImpl implements BlockBankAccountController {

  private final BlockBankAccountUseCase useCase;

  @Override
  public ResponseEntity<Void> block(UUID bankAccountId) {
    useCase.execute(new BlockBankAccountCommand(BankAccountId.of(bankAccountId)));
    return ResponseEntity.noContent().build();
  }
}