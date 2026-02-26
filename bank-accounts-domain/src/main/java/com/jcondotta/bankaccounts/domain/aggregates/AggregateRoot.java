package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.domain.value_objects.AggregateId;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot<ID extends AggregateId> {

  private final ID id;
  private final List<DomainEvent> events = new ArrayList<>();

  protected AggregateRoot(ID id) {
    this.id = id;
  }

  public ID id() {
    return id;
  }

  protected void registerEvent(DomainEvent event) {
    events.add(event);
  }

  public List<DomainEvent> pullEvents() {
    var unmodifiableEvents = List.copyOf(events);
    events.clear();
    return unmodifiableEvents;
  }
}