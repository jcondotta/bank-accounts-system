package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;

import java.util.UUID;

public interface DomainEventToIntegrationResolver {

  IntegrationEvent<?> toIntegrationEvent(DomainEvent event, UUID correlationId);
}