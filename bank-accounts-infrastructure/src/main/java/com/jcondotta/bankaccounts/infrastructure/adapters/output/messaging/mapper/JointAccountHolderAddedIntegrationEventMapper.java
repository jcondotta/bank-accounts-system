package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;
import com.jcondotta.bankaccounts.contracts.addholder.JointAccountHolderAddedIntegrationEvent;
import com.jcondotta.bankaccounts.contracts.addholder.JointAccountHolderAddedIntegrationPayload;
import com.jcondotta.banking.accounts.domain.bankaccount.events.BankAccountJointHolderAddedEvent;
import org.springframework.stereotype.Component;

@Component
public class JointAccountHolderAddedIntegrationEventMapper extends AbstractDomainEventMapper<BankAccountJointHolderAddedEvent> {

  public JointAccountHolderAddedIntegrationEventMapper() {
    super(BankAccountJointHolderAddedEvent.class);
  }

  @Override
  protected IntegrationEvent<?> buildIntegrationEvent(BankAccountJointHolderAddedEvent event, IntegrationEventMetadata metadata) {
    JointAccountHolderAddedIntegrationPayload payload = new JointAccountHolderAddedIntegrationPayload(
      event.aggregateId().value(),
      event.accountHolderId().value()
    );

    return new JointAccountHolderAddedIntegrationEvent(metadata, payload);
  }
}