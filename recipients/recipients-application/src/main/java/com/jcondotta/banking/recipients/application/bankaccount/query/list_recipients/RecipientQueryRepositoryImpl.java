package com.jcondotta.banking.recipients.application.bankaccount.query.list_recipients;

import com.jcondotta.banking.recipients.domain.recipient.identity.BankAccountId;
import com.jcondotta.banking.recipients.domain.recipient.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class RecipientQueryRepositoryImpl implements RecipientQueryRepository {

  private final BankAccountRepository bankAccountRepository;

  @Override
  public List<RecipientView> findActiveByBankAccountId(BankAccountId bankAccountId) {

    return bankAccountRepository.findById(bankAccountId)
      .map(bankAccount -> RecipientViewMapper.toViewList(bankAccount.getRecipients()))
      .orElse(List.of());
  }
}