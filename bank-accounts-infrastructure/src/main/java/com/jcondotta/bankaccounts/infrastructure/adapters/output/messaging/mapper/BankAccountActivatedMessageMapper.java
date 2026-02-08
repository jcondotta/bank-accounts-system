package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.domain.events.BankAccountActivatedEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventMetadata;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.BankAccountActivatedMessage;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.BankAccountOpenedMessage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankAccountActivatedMessageMapper {

  public EventEnvelope<BankAccountActivatedMessage> toMessage(BankAccountActivatedEvent event) {
    BankAccountActivatedMessage payload =
      new BankAccountActivatedMessage(
        event.bankAccountId().value(),
        event.occurredAt().toInstant().toString()
      );

    EventMetadata metadata = EventMetadata.from(event.eventId().value().toString(), UUID.randomUUID().toString(), event.eventType().value());

    return EventEnvelope.of(metadata, payload);
  }
}
