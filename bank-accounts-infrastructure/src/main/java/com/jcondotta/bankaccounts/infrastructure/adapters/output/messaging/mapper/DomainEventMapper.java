package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;

import java.util.UUID;

public interface DomainEventMapper {

  Class<? extends DomainEvent> mappedEventType();

  IntegrationEvent<?> toIntegrationEvent(DomainEvent event, UUID correlationId);
}