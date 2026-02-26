package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.DefaultEventMetadata;
import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;

import java.util.Objects;

public abstract class AbstractDomainEventMapper<E extends DomainEvent> implements DomainEventMapper<E> {

  public static final int METADATA_VERSION = 1;

  private final Class<E> eventType;

  protected AbstractDomainEventMapper(Class<E> eventType) {
    this.eventType = Objects.requireNonNull(eventType, "eventType must not be null");
  }

  @Override
  public Class<E> mappedEventType() {
    return eventType;
  }

  @Override
  public final IntegrationEvent<?> toIntegrationEvent(E event, EventMetadataContext context) {
    Objects.requireNonNull(event, "event must not be null");
    Objects.requireNonNull(context, "eventMetadataContext must not be null");

    IntegrationEventMetadata metadata = buildMetadata(event, context);
    return buildIntegrationEvent(event, metadata);
  }

  protected IntegrationEventMetadata buildMetadata(E event, EventMetadataContext context) {
    return DefaultEventMetadata.create(
      event.eventId().value(),
      context.correlationId(),
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