package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount;

import com.jcondotta.bankaccounts.application.usecase.open.OpenBankAccountUseCase;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.mapper.OpenBankAccountRequestControllerMapper;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.OpenBankAccountRequest;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountsURIProperties;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
public class OpenBankAccountControllerImpl implements OpenBankAccountController {

  private final OpenBankAccountUseCase useCase;
  private final OpenBankAccountRequestControllerMapper mapper;
  private final BankAccountsURIProperties uriProperties;

  @Override
  @Timed(
      value = "bankAccounts.open.time",
      description = "bank account opening time measurement",
      percentiles = {0.5, 0.95, 0.99})
  public ResponseEntity<Void> openBankAccount(OpenBankAccountRequest request) {
    var command = mapper.toCommand(request);
    var openBankAccountResult = useCase.execute(command);

    return ResponseEntity
      .created(uriProperties.bankAccountURI(openBankAccountResult.bankAccountId().value()))
      .build();
  }
}
