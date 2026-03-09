package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount;

import com.jcondotta.bankaccounts.application.usecase.lookup.BankAccountLookupUseCase;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.mapper.BankAccountLookupResponseControllerMapper;
import com.jcondotta.banking.accounts.domain.bankaccount.identity.BankAccountId;
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
public class BankAccountLookupControllerImpl implements BankAccountLookupController {

  private final BankAccountLookupUseCase bankAccountLookupUseCase;
  private final BankAccountLookupResponseControllerMapper mapper;

  public ResponseEntity<BankAccountDetailsResponse> getBankAccount(UUID bankAccountId) {
    log.info("Received request to retrieve bank account [id={}]", bankAccountId);

    var bankAccountLookupResult = bankAccountLookupUseCase.lookup(BankAccountId.of(bankAccountId));
    return ResponseEntity.ok(mapper.toBankAccountDetailsResponse(bankAccountLookupResult));
  }
}