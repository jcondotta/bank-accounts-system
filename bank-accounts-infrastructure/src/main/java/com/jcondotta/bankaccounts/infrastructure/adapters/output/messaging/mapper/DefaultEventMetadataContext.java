package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import java.util.Objects;
import java.util.UUID;

public record DefaultEventMetadataContext(UUID correlationId) implements EventMetadataContext {

  public DefaultEventMetadataContext {
    Objects.requireNonNull(correlationId, "correlationId must not be null");
  }

  public static DefaultEventMetadataContext of(UUID correlationId) {
    return new DefaultEventMetadataContext(correlationId);
  }
}