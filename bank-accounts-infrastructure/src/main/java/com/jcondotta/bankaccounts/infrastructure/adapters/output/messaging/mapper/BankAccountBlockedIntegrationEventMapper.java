package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;
import com.jcondotta.bankaccounts.contracts.block.BankAccountBlockedIntegrationEvent;
import com.jcondotta.bankaccounts.contracts.block.BankAccountBlockedIntegrationPayload;
import com.jcondotta.bankaccounts.domain.events.BankAccountBlockedEvent;
import org.springframework.stereotype.Component;

@Component
public class BankAccountBlockedIntegrationEventMapper extends AbstractDomainEventMapper<BankAccountBlockedEvent> {

  public BankAccountBlockedIntegrationEventMapper() {
    super(BankAccountBlockedEvent.class);
  }

  @Override
  protected IntegrationEvent<?> buildIntegrationEvent(BankAccountBlockedEvent event, IntegrationEventMetadata metadata) {
    BankAccountBlockedIntegrationPayload payload = new BankAccountBlockedIntegrationPayload(
      event.bankAccountId().value()
    );

    return new BankAccountBlockedIntegrationEvent(metadata, payload);
  }
}