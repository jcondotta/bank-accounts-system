package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.events.types.DomainEventType;
import com.jcondotta.bankaccounts.domain.value_objects.EventId;

import java.time.Instant;

/**
 * Marker interface for domain events.
 * Represents a fact that has occurred in the domain.
 */
public sealed interface DomainEvent permits BankAccountEvent {
  EventId eventId();
  DomainEventType eventType();
  Instant occurredAt();
}