package com.jcondotta.banking.recipients.application.bankaccount.query.list_recipients;

import com.jcondotta.banking.recipients.domain.recipient.aggregate.Recipient;

import java.util.List;

public final class RecipientViewMapper {

  private RecipientViewMapper() {
  }

  public static RecipientView toView(Recipient recipient) {
    return new RecipientView(
      recipient.getId().value(),
      recipient.getRecipientName().value(),
      recipient.getIban().value(),
      recipient.getCreatedAt()
    );
  }

  public static List<RecipientView> toViewList(List<Recipient> recipients) {
    return recipients.stream()
      .map(RecipientViewMapper::toView)
      .toList();
  }
}