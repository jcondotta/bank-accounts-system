package com.jcondotta.banking.recipients.infrastructure.bankaccount.adapters.input.rest.list_recipients;

import com.jcondotta.application.core.query.QueryHandler;
import com.jcondotta.banking.recipients.application.bankaccount.query.list_recipients.ListRecipientsQuery;
import com.jcondotta.banking.recipients.application.bankaccount.query.list_recipients.RecipientView;
import com.jcondotta.banking.recipients.domain.recipient.identity.BankAccountId;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@AllArgsConstructor
public class ListRecipientsControllerImpl implements ListRecipientsController {

  private final QueryHandler<ListRecipientsQuery, List<RecipientView>> queryHandler;

  @Override
  public List<RecipientView> listRecipients(UUID bankAccountId) {

    var query = new ListRecipientsQuery(
      BankAccountId.of(bankAccountId)
    );

    return queryHandler.handle(query);
  }
}