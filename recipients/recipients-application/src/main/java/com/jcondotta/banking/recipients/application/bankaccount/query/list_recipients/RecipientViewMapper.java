package com.jcondotta.banking.recipients.application.bankaccount.query.list_recipients;

import com.jcondotta.banking.recipients.domain.recipient.aggregate.Recipient;

import java.util.List;

final class RecipientViewMapper {

  private RecipientViewMapper() {
  }

  static RecipientView toView(Recipient recipient) {
    return new RecipientView(
      recipient.getId(),
      recipient.getRecipientName(),
      recipient.getIban(),
      recipient.getCreatedAt()
    );
  }

  static List<RecipientView> toViewList(List<Recipient> recipients) {
    return recipients.stream()
      .map(RecipientViewMapper::toView)
      .toList();
  }
}