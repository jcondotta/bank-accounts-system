package com.jcondotta.domain.model;

import com.jcondotta.domain.events.DomainEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot<ID extends EntityId<?>> extends Entity<ID> {

  private final List<DomainEvent> events = new ArrayList<>();

  protected AggregateRoot(ID id) {
    super(id);
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