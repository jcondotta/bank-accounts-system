package com.jcondotta.bankaccounts.application.usecase.lookupbankaccount;

import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.lookupbankaccount.BankAccountLookupRepository;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.mapper.BankAccountLookupResultMapper;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountLookupResult;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BankAccountLookupUseCaseImpl implements BankAccountLookupUseCase {

    private final BankAccountLookupRepository bankAccountLookupRepository;
    private final BankAccountLookupResultMapper lookupResultMapper;

    @Override
    public BankAccountLookupResult lookup(BankAccountId bankAccountId) {
        return bankAccountLookupRepository.byId(bankAccountId)
          .map(lookupResultMapper::toResult)
          .orElseThrow(() -> new BankAccountNotFoundException(bankAccountId));
    }
}