package com.jcondotta.banking.contracts;

import java.time.Instant;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record DefaultIntegrationEventMetadata(
  UUID eventId,
  UUID correlationId,
  String eventType,
  int version,
  Instant occurredAt

) implements IntegrationEventMetadata {

  public DefaultIntegrationEventMetadata {
    requireNonNull(eventId, "eventId must not be null");
    requireNonNull(correlationId, "correlationId must not be null");
    requireNonNull(eventType, "eventType must not be null");
    requireNonNull(occurredAt, "occurredAt must not be null");

    if (version <= 0) {
      throw new IllegalArgumentException("version must be greater than zero");
    }
  }

  public static DefaultIntegrationEventMetadata create(UUID eventId, UUID correlationId, String eventType, int version, Instant occurredAt) {
    return new DefaultIntegrationEventMetadata(eventId, correlationId, eventType, version, occurredAt);
  }
}
