package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.kafka;

import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountBlockedEventPublisher;
import com.jcondotta.bankaccounts.domain.events.BankAccountBlockedEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.BankAccountBlockedMessageMapper;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.BankAccountBlockedMessage;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountActivatedTopicProperties;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountBlockedTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBankAccountBlockedEventPublisher implements BankAccountBlockedEventPublisher {

  private final KafkaTemplate<String, EventEnvelope<BankAccountBlockedMessage>> kafkaTemplate;
  private final BankAccountBlockedMessageMapper messageMapper;
  private final BankAccountBlockedTopicProperties topicProperties;

  @Override
  public void publish(DomainEvent event) {
    if (event instanceof BankAccountBlockedEvent bankAccountBlockedEvent) {

      log.info(
        "Publishing BankAccountBlockedEvent to Kafka [topic={}, key={}]",
        topicProperties.topicName(),
        bankAccountBlockedEvent.bankAccountId()
      );

      var producerRecord = new ProducerRecord<>(
        topicProperties.topicName(),
        bankAccountBlockedEvent.bankAccountId().value().toString(),
        messageMapper.toMessage(bankAccountBlockedEvent)
      );

      kafkaTemplate.send(producerRecord);
    }
  }
}
