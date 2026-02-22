package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.DefaultEventMetadata;
import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;

import java.util.UUID;

public abstract class AbstractDomainEventMapper<E extends DomainEvent> implements DomainEventMapper {

  public static final int METADATA_VERSION = 1;

  private final Class<E> eventType;

  protected AbstractDomainEventMapper(Class<E> eventType) {
    this.eventType = eventType;
  }

  @Override
  public Class<? extends DomainEvent> mappedEventType() {
    return eventType;
  }

  @Override
  public final IntegrationEvent<?> toIntegrationEvent(DomainEvent event, UUID correlationId) {
    E typedEvent = eventType.cast(event);

    IntegrationEventMetadata metadata = buildMetadata(typedEvent, correlationId);

    return buildIntegrationEvent(typedEvent, metadata);
  }

  protected IntegrationEventMetadata buildMetadata(E event, UUID correlationId) {
    return DefaultEventMetadata.create(
      event.eventId().value(),
      correlationId,
      event.eventType().value(),
      eventMetadataVersion(),
      event.occurredAt()
    );
  }

  protected int eventMetadataVersion() {
    return METADATA_VERSION;
  }

  protected abstract IntegrationEvent<?> buildIntegrationEvent(E event, IntegrationEventMetadata metadata);
}