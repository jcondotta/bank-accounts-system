package com.jcondotta.bankaccounts.application.ports.output.messaging;

import com.jcondotta.bankaccounts.domain.events.DomainEvent;

public interface DomainEventDispatcher {

  void publish(DomainEvent event);
}
