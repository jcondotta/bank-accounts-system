package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public record EventMetadata(

  String eventId,
  String correlationId,
  String eventType,

  @JsonFormat(
    shape = JsonFormat.Shape.STRING,
    pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
    timezone = "UTC"
  )
  Instant publishedAt

) {

  public EventMetadata {
    requireNonNull(eventId, "eventId must not be null");
    requireNonNull(correlationId, "correlationId must not be null");
    requireNonNull(eventType, "eventType must not be null");
    requireNonNull(publishedAt, "publishedAt must not be null");
  }

  public static EventMetadata from(String eventId, String correlationId, String eventType) {
    return new EventMetadata(
      eventId,
      correlationId,
      eventType,
      Instant.now()
    );
  }
}
