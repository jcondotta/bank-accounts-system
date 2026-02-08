package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.domain.events.JointAccountHolderAddedEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventMetadata;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.JointAccountHolderAddedMessage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JointAccountHolderAddedMessageMapper {

  public EventEnvelope<JointAccountHolderAddedMessage> toMessage(JointAccountHolderAddedEvent event) {
    JointAccountHolderAddedMessage payload =
      new JointAccountHolderAddedMessage(
        event.bankAccountId().value(),
        event.accountHolderId().value(),
        event.occurredAt().toInstant().toString()
      );

    EventMetadata metadata = EventMetadata.from(event.eventId().value().toString(), UUID.randomUUID().toString(), event.eventType().value());

    return EventEnvelope.of(metadata, payload);
  }
}
