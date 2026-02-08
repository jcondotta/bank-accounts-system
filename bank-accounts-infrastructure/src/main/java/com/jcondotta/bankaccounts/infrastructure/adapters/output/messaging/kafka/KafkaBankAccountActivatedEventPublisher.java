package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.kafka;

import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountActivatedEventPublisher;
import com.jcondotta.bankaccounts.domain.events.BankAccountActivatedEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.BankAccountActivatedMessage;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.BankAccountActivatedMessageMapper;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountActivatedTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBankAccountActivatedEventPublisher implements BankAccountActivatedEventPublisher {

  private final KafkaTemplate<String, EventEnvelope<BankAccountActivatedMessage>> kafkaTemplate;
  private final BankAccountActivatedMessageMapper messageMapper;
  private final BankAccountActivatedTopicProperties topicProperties;

  @Override
  public void publish(DomainEvent event) {
    if (event instanceof BankAccountActivatedEvent bankAccountActivatedEvent) {

      log.info(
        "Publishing BankAccountActivatedEvent to Kafka [topic={}, key={}]",
        topicProperties.topicName(),
        bankAccountActivatedEvent.bankAccountId()
      );

      var producerRecord = new ProducerRecord<>(
        topicProperties.topicName(),
        bankAccountActivatedEvent.bankAccountId().value().toString(),
        messageMapper.toMessage(bankAccountActivatedEvent)
      );

      kafkaTemplate.send(producerRecord);
    }
  }
}
