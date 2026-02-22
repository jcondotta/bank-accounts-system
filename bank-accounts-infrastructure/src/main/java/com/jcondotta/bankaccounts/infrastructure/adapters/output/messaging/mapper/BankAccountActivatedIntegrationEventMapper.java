package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;
import com.jcondotta.bankaccounts.contracts.activate.BankAccountActivatedIntegrationEvent;
import com.jcondotta.bankaccounts.contracts.activate.BankAccountActivatedIntegrationPayload;
import com.jcondotta.bankaccounts.domain.events.BankAccountActivatedEvent;
import org.springframework.stereotype.Component;

@Component
public class BankAccountActivatedIntegrationEventMapper extends AbstractDomainEventMapper<BankAccountActivatedEvent> {

  public BankAccountActivatedIntegrationEventMapper() {
    super(BankAccountActivatedEvent.class);
  }

  @Override
  protected IntegrationEvent<?> buildIntegrationEvent(BankAccountActivatedEvent event, IntegrationEventMetadata metadata) {
    BankAccountActivatedIntegrationPayload payload = new BankAccountActivatedIntegrationPayload(
      event.bankAccountId().value()
    );

    return new BankAccountActivatedIntegrationEvent(metadata, payload);
  }
}