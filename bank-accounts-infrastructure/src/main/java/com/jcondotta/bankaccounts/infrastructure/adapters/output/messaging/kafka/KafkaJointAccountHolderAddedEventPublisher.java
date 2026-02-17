package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.kafka;

import com.jcondotta.bankaccounts.application.ports.output.messaging.JointAccountHolderAddedEventPublisher;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.domain.events.JointAccountHolderAddedEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.JointAccountHolderAddedMessageMapper;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.JointAccountHolderAddedMessage;
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

  private final KafkaTemplate<String, EventEnvelope<JointAccountHolderAddedMessage>> kafkaTemplate;
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
