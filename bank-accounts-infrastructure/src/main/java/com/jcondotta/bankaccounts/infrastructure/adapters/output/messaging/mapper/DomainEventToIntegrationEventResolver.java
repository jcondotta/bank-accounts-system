package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.domain.events.DomainEvent;

public interface DomainEventToIntegrationEventResolver {

  IntegrationEvent<?> toIntegrationEvent(DomainEvent event, EventMetadataContext eventMetadataContext);
}