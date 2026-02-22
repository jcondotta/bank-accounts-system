package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.domain.value_objects.AggregateId;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.OutboxEntity;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.OutboxEntityKey;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.OutboxKey;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.OutboxKeyFactory;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.enums.EntityType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class OutboxEntityMapper {

  private final ObjectMapper objectMapper;

  public OutboxEntity toOutboxEntity(AggregateId aggregateId, IntegrationEvent<?> integrationEvent) {
    OutboxKey key = OutboxKeyFactory.pending(
      aggregateId.value(),
      integrationEvent.metadata().eventId(),
      Instant.now()
    );

    return OutboxEntity.builder()
      .partitionKey(key.partitionKey())
      .sortKey(key.sortKey())
      .gsi1pk(key.gsi1pk())
      .gsi1sk(key.gsi1sk())
      .entityType(EntityType.OUTBOX_EVENT)
      .eventId(integrationEvent.metadata().eventId())
      .aggregateId(aggregateId.value())
      .eventType(integrationEvent.metadata().eventType())
      .version(integrationEvent.metadata().version())
      .payload(serialize(integrationEvent))
//      .status(OutboxStatus.PENDING)
      .publishedAt(null)
      .build();
  }

  private String serialize(IntegrationEvent<?> event) {
    try {
      return objectMapper.writeValueAsString(event);
    }
    catch (JsonProcessingException e) {
      throw new IllegalStateException("Failed to serialize integration event: " + event.getClass().getName(), e);
    }
  }
}
