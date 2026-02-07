package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.value_objects.EventId;

import java.time.ZonedDateTime;

public interface DomainEvent {
  EventId eventId();
  ZonedDateTime occurredAt();
}