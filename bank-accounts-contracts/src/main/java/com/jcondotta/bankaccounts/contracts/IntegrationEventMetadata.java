package com.jcondotta.bankaccounts.contracts;

import java.time.Instant;
import java.util.UUID;

public interface IntegrationEventMetadata {

  UUID eventId();
  UUID correlationId();
  String eventType();
  int version();
  Instant occurredAt();
}