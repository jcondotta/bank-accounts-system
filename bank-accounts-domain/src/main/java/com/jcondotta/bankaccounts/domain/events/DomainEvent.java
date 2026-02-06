package com.jcondotta.bankaccounts.domain.events;

import java.time.ZonedDateTime;

public interface DomainEvent {
  ZonedDateTime occurredAt();
}