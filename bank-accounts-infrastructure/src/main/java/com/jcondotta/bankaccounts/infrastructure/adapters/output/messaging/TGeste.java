package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging;

import com.jcondotta.bankaccounts.application.ports.output.messaging.DomainEventPublisher;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public class TGeste implements DomainEventPublisher {
  @Override
  public void publish(DomainEvent event) {
    System.out.println("Publishing event: " + event.getClass().getSimpleName());
  }
}
