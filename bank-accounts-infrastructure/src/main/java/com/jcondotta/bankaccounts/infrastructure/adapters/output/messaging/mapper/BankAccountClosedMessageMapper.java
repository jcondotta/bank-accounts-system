package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.domain.events.BankAccountClosedEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventMetadata;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.BankAccountClosedMessage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankAccountClosedMessageMapper {

  public EventEnvelope<BankAccountClosedMessage> toMessage(BankAccountClosedEvent event) {
    BankAccountClosedMessage payload =
      new BankAccountClosedMessage(
        event.bankAccountId().value(),
        event.occurredAt().toInstant().toString()
      );

    EventMetadata metadata = EventMetadata.from(event.eventId().value().toString(), UUID.randomUUID().toString(), event.eventType().value());

    return EventEnvelope.of(metadata, payload);
  }
}
