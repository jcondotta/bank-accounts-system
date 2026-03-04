package com.jcondotta.domain.events;


import com.jcondotta.domain.model.EntityId;

import java.time.Instant;

public interface DomainEvent {

  EventId eventId();
  EntityId<?> aggregateId();
  Instant occurredAt();

  default String eventName() {
    return this.getClass().getSimpleName();
  }
}