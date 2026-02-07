package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging;

import com.jcondotta.bankaccounts.domain.events.BankAccountActivatedEvent;
import org.springframework.stereotype.Component;

@Component
public class BankAccountActivatedMessageMapper {

  public BankAccountActivatedMessage toMessage(BankAccountActivatedEvent event) {
    return new BankAccountActivatedMessage(
        event.eventId().value(),
        event.bankAccountId().value(),
        event.occurredAt().toInstant().toString()
    );
  }
}
