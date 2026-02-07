package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging;

import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountOpenedEventPublisher;
import com.jcondotta.bankaccounts.domain.events.BankAccountOpenedEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountOpenedTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBankAccountOpenedEventPublisher implements BankAccountOpenedEventPublisher {

  private static final String IDEMPOTENCY_KEY_HEADER = "idempotency-key";

  private final KafkaTemplate<String, BankAccountOpenedMessage> kafkaTemplate;
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
        messageMapper.toMessage(bankAccountOpenedEvent)
      );

      kafkaTemplate.send(producerRecord);
    }
  }
}
