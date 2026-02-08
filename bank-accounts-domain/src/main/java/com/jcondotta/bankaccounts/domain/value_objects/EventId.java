package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import com.jcondotta.bankaccounts.domain.validation.DomainEventValidationErrors;

import java.util.UUID;

public record EventId(UUID value) {

  public EventId {
    if (value == null) {
      throw new DomainValidationException(DomainEventValidationErrors.EVENT_ID_NOT_NULL);
    }
  }

  public static EventId newId() {
    return new EventId(UUID.randomUUID());
  }

  public static EventId of(UUID value) {
    return new EventId(value);
  }
}
