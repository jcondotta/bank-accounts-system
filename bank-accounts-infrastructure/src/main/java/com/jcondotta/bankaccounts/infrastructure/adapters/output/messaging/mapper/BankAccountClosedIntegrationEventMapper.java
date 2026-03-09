package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;
import com.jcondotta.bankaccounts.contracts.close.BankAccountClosedIntegrationEvent;
import com.jcondotta.bankaccounts.contracts.close.BankAccountClosedIntegrationPayload;
import com.jcondotta.banking.accounts.domain.bankaccount.events.BankAccountClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class BankAccountClosedIntegrationEventMapper extends AbstractDomainEventMapper<BankAccountClosedEvent> {

  public BankAccountClosedIntegrationEventMapper() {
    super(BankAccountClosedEvent.class);
  }

  @Override
  protected IntegrationEvent<?> buildIntegrationEvent(BankAccountClosedEvent event, IntegrationEventMetadata metadata) {
    BankAccountClosedIntegrationPayload payload = new BankAccountClosedIntegrationPayload(
      event.aggregateId().value()
    );

    return new BankAccountClosedIntegrationEvent(metadata, payload);
  }
}