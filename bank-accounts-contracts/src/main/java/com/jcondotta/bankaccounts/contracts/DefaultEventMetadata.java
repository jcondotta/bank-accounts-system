package com.jcondotta.bankaccounts.contracts;

import java.time.Instant;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record DefaultEventMetadata(

  UUID eventId,
  UUID correlationId,
  String eventType,
  int version,
  Instant occurredAt

) implements IntegrationEventMetadata {

  public DefaultEventMetadata {
    requireNonNull(eventId, "eventId must not be null");
    requireNonNull(correlationId, "correlationId must not be null");
    requireNonNull(eventType, "eventType must not be null");
    requireNonNull(occurredAt, "occurredAt must not be null");

    if (version <= 0) {
      throw new IllegalArgumentException("version must be greater than zero");
    }
  }

  public static DefaultEventMetadata create(UUID eventId, UUID correlationId, String eventType, int version, Instant occurredAt) {
    return new DefaultEventMetadata(eventId, correlationId, eventType, version, occurredAt);
  }
}
