package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.domain.events.BankAccountBlockedEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventMetadata;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.BankAccountBlockedMessage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankAccountBlockedMessageMapper {

  public EventEnvelope<BankAccountBlockedMessage> toMessage(BankAccountBlockedEvent event) {
    BankAccountBlockedMessage payload =
      new BankAccountBlockedMessage(
        event.bankAccountId().value(),
        event.occurredAt().toInstant().toString()
      );

    EventMetadata metadata = EventMetadata.from(event.eventId().value().toString(), UUID.randomUUID().toString(), event.eventType().value());

    return EventEnvelope.of(metadata, payload);
  }
}
