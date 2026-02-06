package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount;

import com.jcondotta.bankaccounts.application.usecase.openbankaccount.OpenBankAccountUseCase;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountsURIProperties;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
public class OpenBankAccountControllerImpl implements OpenBankAccountController {

  private final OpenBankAccountUseCase useCase;
  private final BankAccountsURIProperties uriProperties;

  @Override
  @Timed(
      value = "bankAccounts.open.time",
      description = "bank account opening time measurement",
      percentiles = {0.5, 0.95, 0.99})
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> openBankAccount() {
//    var command = mapper.toCommand(bankAccountId, request);
//    useCase.execute(command);

    return ResponseEntity.created(uriProperties.bankAccountURI(BankAccountId.newId().value())).build();
  }
}
