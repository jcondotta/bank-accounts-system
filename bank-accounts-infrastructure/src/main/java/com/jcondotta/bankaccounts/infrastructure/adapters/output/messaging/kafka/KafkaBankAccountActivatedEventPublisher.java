package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.kafka;

import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountActivatedEventPublisher;
import com.jcondotta.bankaccounts.domain.events.BankAccountActivatedEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.BankAccountActivatedIntegrationEventMapper;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountActivatedTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBankAccountActivatedEventPublisher implements BankAccountActivatedEventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final BankAccountActivatedIntegrationEventMapper messageMapper;
  private final BankAccountActivatedTopicProperties topicProperties;

  @Override
  public void publish(DomainEvent event) {
    if (event instanceof BankAccountActivatedEvent bankAccountActivatedEvent) {

      log.info(
        "Publishing BankAccountActivatedEvent to Kafka [topic={}, key={}]",
        topicProperties.topicName(),
        bankAccountActivatedEvent.bankAccountId()
      );

//      var producerRecord = new ProducerRecord<String, Object>(
//        topicProperties.topicName(),
//        bankAccountActivatedEvent.bankAccountId().value().toString(),
//        messageMapper.toMessage(bankAccountActivatedEvent)
//      );
//
//      kafkaTemplate.send(producerRecord);
    }
  }
}
