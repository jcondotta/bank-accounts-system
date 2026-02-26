package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;

public interface DomainEventMapper<E extends DomainEvent> {

  Class<E> mappedEventType();

  IntegrationEvent<?> toIntegrationEvent(
    E event,
    EventMetadataContext context
  );
}