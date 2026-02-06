package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("${api.v1.bank-accounts.root-path}")
public interface OpenBankAccountController {

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> openBankAccount();
}
