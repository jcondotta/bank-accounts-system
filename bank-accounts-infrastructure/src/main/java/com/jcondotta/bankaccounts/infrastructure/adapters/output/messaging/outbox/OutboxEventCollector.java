package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.outbox;

import com.jcondotta.bankaccounts.domain.entities.AggregateRoot;
import com.jcondotta.bankaccounts.infrastructure.adapters.CorrelationIdProvider;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.DomainEventToIntegrationResolver;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.OutboxEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OutboxEventCollector {

  private final DomainEventToIntegrationResolver domainEventToIntegrationEventMapper;
  private final OutboxEntityMapper outboxMapper;
  private final CorrelationIdProvider correlationIdProvider;

  public List<OutboxEntity> collect(AggregateRoot<?> aggregate) {
    Objects.requireNonNull(aggregate, "aggregate must not be null");

    var aggregateId = aggregate.id();
    var correlationId = correlationIdProvider.get();

    return aggregate.pullEvents()
      .stream()
      .map(event -> {
        var integrationEvent = domainEventToIntegrationEventMapper.toIntegrationEvent(event, correlationId);

        return outboxMapper.toOutboxEntity(aggregateId, integrationEvent);
      })
      .toList();
  }
}