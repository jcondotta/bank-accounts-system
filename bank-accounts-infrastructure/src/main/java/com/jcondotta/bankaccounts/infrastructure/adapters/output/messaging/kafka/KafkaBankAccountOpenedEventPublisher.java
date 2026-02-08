package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.kafka;

import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountOpenedEventPublisher;
import com.jcondotta.bankaccounts.domain.events.BankAccountOpenedEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.BankAccountOpenedMessage;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.BankAccountOpenedMessageMapper;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountOpenedTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBankAccountOpenedEventPublisher implements BankAccountOpenedEventPublisher {

  private final KafkaTemplate<String, EventEnvelope<BankAccountOpenedMessage>> kafkaTemplate;
  private final BankAccountOpenedMessageMapper messageMapper;
  private final BankAccountOpenedTopicProperties topicProperties;

  @Override
  public void publish(DomainEvent event) {
    if (event instanceof BankAccountOpenedEvent bankAccountOpenedEvent) {

      log.info(
        "Publishing BankAccountOpenedEvent to Kafka [topic={}, key={}]",
        topicProperties.topicName(),
        bankAccountOpenedEvent.bankAccountId()
      );

      var producerRecord = new ProducerRecord<>(
        topicProperties.topicName(),
        bankAccountOpenedEvent.bankAccountId().value().toString(),
        messageMapper.toEnvelope(bankAccountOpenedEvent, UUID.randomUUID().toString())
      );

      kafkaTemplate.send(producerRecord);
    }
  }
}
