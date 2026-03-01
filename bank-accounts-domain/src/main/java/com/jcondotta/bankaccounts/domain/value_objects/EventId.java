package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.validation.DomainPreconditions;

import java.util.UUID;

public record EventId(UUID value) {

  public static final String EVENT_ID_NOT_PROVIDED = "Event id must be provided.";

  public EventId {
    DomainPreconditions.required(value, EVENT_ID_NOT_PROVIDED);
  }

  public static EventId newId() {
    return new EventId(UUID.randomUUID());
  }

  public static EventId of(UUID value) {
    return new EventId(value);
  }
}
