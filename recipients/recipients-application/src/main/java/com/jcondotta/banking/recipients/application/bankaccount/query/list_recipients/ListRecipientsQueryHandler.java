package com.jcondotta.banking.recipients.application.bankaccount.query.list_recipients;

import com.jcondotta.application.core.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListRecipientsQueryHandler
  implements QueryHandler<ListRecipientsQuery, List<RecipientView>> {

  private final RecipientQueryRepository queryRepository;

  @Override
  public List<RecipientView> handle(ListRecipientsQuery query) {
    return queryRepository.findActiveByBankAccountId(query.bankAccountId());
  }
}