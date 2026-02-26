package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import java.util.UUID;

public interface EventMetadataContext {
  UUID correlationId();
}

