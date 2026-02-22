package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;
import com.jcondotta.bankaccounts.contracts.open.BankAccountOpenedIntegrationEvent;
import com.jcondotta.bankaccounts.contracts.open.BankAccountOpenedIntegrationPayload;
import com.jcondotta.bankaccounts.domain.events.BankAccountOpenedEvent;
import org.springframework.stereotype.Component;

@Component
public class BankAccountOpenedIntegrationEventMapper extends AbstractDomainEventMapper<BankAccountOpenedEvent> {

  public BankAccountOpenedIntegrationEventMapper() {
    super(BankAccountOpenedEvent.class);
  }

  @Override
  protected IntegrationEvent<?> buildIntegrationEvent(BankAccountOpenedEvent event, IntegrationEventMetadata metadata) {
    BankAccountOpenedIntegrationPayload payload =
      new BankAccountOpenedIntegrationPayload(
        event.bankAccountId().value(),
        event.accountType().name(),
        event.currency().name(),
        event.primaryAccountHolderId().value()
      );

    return new BankAccountOpenedIntegrationEvent(metadata, payload);
  }
}