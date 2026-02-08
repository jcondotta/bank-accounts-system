package com.jcondotta.bankaccounts.application.usecase.lookupbankaccount;

import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.LookupBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.mapper.BankAccountDetailsMapper;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BankAccountLookupUseCaseImpl implements BankAccountLookupUseCase {

    private final LookupBankAccountRepository lookupBankAccountRepository;
    private final BankAccountDetailsMapper bankAccountDetailsMapper;

    @Override
    public BankAccountDetails lookup(BankAccountId bankAccountId) {
        var bankAccount = lookupBankAccountRepository.byId(bankAccountId)
          .orElseThrow(() -> new BankAccountNotFoundException(bankAccountId));

        return bankAccountDetailsMapper.toDetails(bankAccount);
    }
}