package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;
import com.jcondotta.bankaccounts.contracts.addholder.JointAccountHolderAddedIntegrationEvent;
import com.jcondotta.bankaccounts.contracts.addholder.JointAccountHolderAddedIntegrationPayload;
import com.jcondotta.bankaccounts.domain.events.JointAccountHolderAddedEvent;
import org.springframework.stereotype.Component;

@Component
public class JointAccountHolderAddedIntegrationEventMapper extends AbstractDomainEventMapper<JointAccountHolderAddedEvent> {

  public JointAccountHolderAddedIntegrationEventMapper() {
    super(JointAccountHolderAddedEvent.class);
  }

  @Override
  protected IntegrationEvent<?> buildIntegrationEvent(JointAccountHolderAddedEvent event, IntegrationEventMetadata metadata) {
    JointAccountHolderAddedIntegrationPayload payload = new JointAccountHolderAddedIntegrationPayload(
      event.bankAccountId().value(),
      event.accountHolderId().value()
    );

    return new JointAccountHolderAddedIntegrationEvent(metadata, payload);
  }
}