package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common;

import static java.util.Objects.requireNonNull;

public record EventEnvelope<T>(EventMetadata metadata, T payload) {

  public EventEnvelope {
    requireNonNull(metadata, "metadata must not be null");
    requireNonNull(payload, "payload must not be null.");
  }

  public static <T> EventEnvelope<T> of(EventMetadata metadata, T payload) {
    return new EventEnvelope<>(metadata, payload);
  }
}