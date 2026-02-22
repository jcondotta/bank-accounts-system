package com.jcondotta.bankaccounts.infrastructure.adapters;

import java.util.UUID;

public interface CorrelationIdProvider {
  UUID get();
}