package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;
import com.jcondotta.bankaccounts.contracts.unblock.BankAccountUnblockedIntegrationEvent;
import com.jcondotta.bankaccounts.contracts.unblock.BankAccountUnblockedIntegrationPayload;
import com.jcondotta.bankaccounts.domain.events.BankAccountUnblockedEvent;
import org.springframework.stereotype.Component;

@Component
public class BankAccountUnblockedIntegrationEventMapper extends AbstractDomainEventMapper<BankAccountUnblockedEvent> {

  public BankAccountUnblockedIntegrationEventMapper() {
    super(BankAccountUnblockedEvent.class);
  }

  @Override
  protected IntegrationEvent<?> buildIntegrationEvent(BankAccountUnblockedEvent event, IntegrationEventMetadata metadata) {
    BankAccountUnblockedIntegrationPayload payload = new BankAccountUnblockedIntegrationPayload(
      event.bankAccountId().value()
    );

    return new BankAccountUnblockedIntegrationEvent(metadata, payload);
  }
}