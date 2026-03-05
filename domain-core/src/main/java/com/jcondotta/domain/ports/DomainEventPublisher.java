package com.jcondotta.domain.ports;

import com.jcondotta.domain.events.DomainEvent;

import java.util.List;

public interface DomainEventPublisher {
  void publish(List<? extends DomainEvent<?>> events);
}