package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.exceptions.DomainEventMapperNotFoundException;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.exceptions.DuplicateDomainEventMapperException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public final class DefaultDomainEventToIntegrationEventResolver
  implements DomainEventToIntegrationResolver {

  private final Map<Class<? extends DomainEvent>, DomainEventMapper> eventMappersByEvent;

  public DefaultDomainEventToIntegrationEventResolver(List<DomainEventMapper> domainEventMappers) {
    Objects.requireNonNull(domainEventMappers, "domainEventMappers must not be null");
    this.eventMappersByEvent = buildRegistry(domainEventMappers);
  }

  @Override
  public IntegrationEvent<?> toIntegrationEvent(
    DomainEvent event,
    UUID correlationId
  ) {
    Objects.requireNonNull(event, "event must not be null");
    Objects.requireNonNull(correlationId, "correlationId must not be null");

    return resolve(event).toIntegrationEvent(event, correlationId);
  }

  private DomainEventMapper resolve(DomainEvent event) {
    DomainEventMapper mapper = eventMappersByEvent.get(event.getClass());

    if (mapper == null) {
      throw new DomainEventMapperNotFoundException(event.getClass());
    }

    return mapper;
  }

  private Map<Class<? extends DomainEvent>, DomainEventMapper> buildRegistry(List<DomainEventMapper> domainEventMappers) {
    Map<Class<? extends DomainEvent>, DomainEventMapper> registry = new HashMap<>();

    for (DomainEventMapper domainEventMapper : domainEventMappers) {
      Objects.requireNonNull(domainEventMapper, "domainEventMapper must not be null");

      Class<? extends DomainEvent> eventType = Objects.requireNonNull(domainEventMapper.mappedEventType(), "mappedEventType must not be null");

      if (registry.putIfAbsent(eventType, domainEventMapper) != null) {
        throw new DuplicateDomainEventMapperException(eventType);
      }
    }

    return Map.copyOf(registry);
  }
}