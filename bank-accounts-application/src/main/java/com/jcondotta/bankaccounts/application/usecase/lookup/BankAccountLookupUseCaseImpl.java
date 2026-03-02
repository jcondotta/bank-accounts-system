package com.jcondotta.bankaccounts.application.usecase.lookup;

import com.jcondotta.bankaccounts.application.usecase.lookup.mapper.BankAccountDetailsMapper;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.repository.BankAccountRepository;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BankAccountLookupUseCaseImpl implements BankAccountLookupUseCase {

  private final BankAccountRepository bankAccountRepository;
  private final BankAccountDetailsMapper bankAccountDetailsMapper;

  @Override
  public BankAccountDetails lookup(BankAccountId bankAccountId) {
    var bankAccount = bankAccountRepository.findById(bankAccountId)
      .orElseThrow(() -> new BankAccountNotFoundException(bankAccountId));

    return bankAccountDetailsMapper.toDetails(bankAccount);
  }
}