package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging;

import com.jcondotta.bankaccounts.domain.events.BankAccountOpenedEvent;
import org.springframework.stereotype.Component;

@Component
public class BankAccountOpenedMessageMapper {

  public BankAccountOpenedMessage toMessage(BankAccountOpenedEvent event) {
    return new BankAccountOpenedMessage(
        event.eventId().value(),
        event.bankAccountId().value(),
        event.accountType().name(),
        event.currency().name(),
        event.iban().value(),
        event.status().name(),
        event.occurredAt().toInstant().toString()
    );
  }
}
