package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging;

import com.jcondotta.bankaccounts.domain.events.JointAccountHolderAddedEvent;
import org.springframework.stereotype.Component;

@Component
public class JointAccountHolderAddedMessageMapper {

  public JointAccountHolderAddedMessage toMessage(JointAccountHolderAddedEvent event) {
    return new JointAccountHolderAddedMessage(
        event.eventId().value(),
        event.bankAccountId().value(),
        event.accountHolderId().value(),
        event.name().value(),
        event.passportNumber().value(),
        event.dateOfBirth().value().toString(), // yyyy-MM-dd
        event.occurredAt().toInstant().toString()
    );
  }
}
