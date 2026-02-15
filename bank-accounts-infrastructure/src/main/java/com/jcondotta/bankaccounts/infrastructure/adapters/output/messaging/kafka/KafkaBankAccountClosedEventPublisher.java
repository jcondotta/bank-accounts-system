package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.kafka;

import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountClosedEventPublisher;
import com.jcondotta.bankaccounts.domain.events.BankAccountClosedEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.BankAccountClosedMessageMapper;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.BankAccountClosedMessage;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountClosedTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBankAccountClosedEventPublisher implements BankAccountClosedEventPublisher {

  private final KafkaTemplate<String, EventEnvelope<BankAccountClosedMessage>> kafkaTemplate;
  private final BankAccountClosedMessageMapper messageMapper;
  private final BankAccountClosedTopicProperties topicProperties;

  @Override
  public void publish(DomainEvent event) {
    if (event instanceof BankAccountClosedEvent bankAccountClosedEvent) {

      log.info(
        "Publishing BankAccountClosedEvent to Kafka [topic={}, key={}]",
        topicProperties.topicName(),
        bankAccountClosedEvent.bankAccountId()
      );

      var producerRecord = new ProducerRecord<>(
        topicProperties.topicName(),
        bankAccountClosedEvent.bankAccountId().value().toString(),
        messageMapper.toMessage(bankAccountClosedEvent)
      );

      kafkaTemplate.send(producerRecord);
    }
  }
}