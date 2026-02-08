package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.domain.events.BankAccountOpenedEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventMetadata;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.BankAccountOpenedMessage;
import org.springframework.stereotype.Component;

@Component
public class BankAccountOpenedMessageMapper {

  public EventEnvelope<BankAccountOpenedMessage> toEnvelope(BankAccountOpenedEvent event, String correlationId) {

    BankAccountOpenedMessage payload =
      new BankAccountOpenedMessage(
        event.bankAccountId().value(),
        event.accountType().name(),
        event.currency().name(),
        event.primaryAccountHolderId().value(),
        event.occurredAt().toInstant().toString()
      );

    EventMetadata metadata = EventMetadata.from(event.eventId().value().toString(), correlationId, event.eventType().value());

    return EventEnvelope.of(metadata, payload);
  }
}
