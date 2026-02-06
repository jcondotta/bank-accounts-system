package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookup;

import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.BankAccountLookupUseCase;
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
public class BankAccountLookupControllerImpl implements BankAccountLookupController {

    private final BankAccountLookupUseCase bankAccountLookupUseCase;
//    private final BankAccountLookupResponseControllerMapper responseControllerMapper;

    public ResponseEntity<Object> findBankAccount(UUID bankAccountId) {
        log.atInfo()
                .setMessage("Received request to retrieve bank account with id: {}")
                .addArgument(bankAccountId)
                .addKeyValue("bankAccountId", bankAccountId)
                .log();

        var bankAccountLookupResult = bankAccountLookupUseCase.lookup(BankAccountId.of(bankAccountId));
        return ResponseEntity.ok(responseControllerMapper.toResponse(bankAccountLookupResult));
    }
}