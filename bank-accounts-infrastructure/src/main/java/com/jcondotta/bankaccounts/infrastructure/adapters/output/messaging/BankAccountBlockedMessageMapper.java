package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging;

import com.jcondotta.bankaccounts.domain.events.BankAccountActivatedEvent;
import com.jcondotta.bankaccounts.domain.events.BankAccountBlockedEvent;
import org.springframework.stereotype.Component;

@Component
public class BankAccountBlockedMessageMapper {

  public BankAccountBlockedMessage toMessage(BankAccountBlockedEvent event) {
    return new BankAccountBlockedMessage(
        event.eventId().value(),
        event.bankAccountId().value(),
        event.occurredAt().toInstant().toString()
    );
  }
}
