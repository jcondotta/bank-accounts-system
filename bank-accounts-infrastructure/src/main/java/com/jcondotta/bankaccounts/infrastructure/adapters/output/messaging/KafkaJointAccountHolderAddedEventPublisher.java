package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging;

import com.jcondotta.bankaccounts.application.ports.output.messaging.JointAccountHolderAddedEventPublisher;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.domain.events.JointAccountHolderAddedEvent;
import com.jcondotta.bankaccounts.infrastructure.properties.JointAccountHolderAddedTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaJointAccountHolderAddedEventPublisher implements JointAccountHolderAddedEventPublisher {

  private static final String IDEMPOTENCY_KEY_HEADER = "idempotency-key";

  private final KafkaTemplate<String, JointAccountHolderAddedMessage> kafkaTemplate;
  private final JointAccountHolderAddedMessageMapper messageMapper;
  private final JointAccountHolderAddedTopicProperties topicProperties;

  @Override
  public void publish(DomainEvent event) {
    if (event instanceof JointAccountHolderAddedEvent jointAccountHolderAddedEvent) {

      log.info(
        "Publishing JointAccountHolderAddedEvent to Kafka [topic={}, key={}]",
        topicProperties.topicName(),
        jointAccountHolderAddedEvent.bankAccountId()
      );

      var producerRecord = new ProducerRecord<>(
        topicProperties.topicName(),
        jointAccountHolderAddedEvent.bankAccountId().value().toString(),
        messageMapper.toMessage(jointAccountHolderAddedEvent)
      );

      kafkaTemplate.send(producerRecord);
    }
  }
}
