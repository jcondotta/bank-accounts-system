package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.kafka;

import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountOpenedEventPublisher;
import com.jcondotta.bankaccounts.domain.events.BankAccountOpenedEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.BankAccountOpenedIntegrationEventMapper;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountOpenedTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBankAccountOpenedEventPublisher implements BankAccountOpenedEventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final BankAccountOpenedIntegrationEventMapper messageMapper;
  private final BankAccountOpenedTopicProperties topicProperties;

  @Override
  public void publish(DomainEvent event) {
    if (event instanceof BankAccountOpenedEvent bankAccountOpenedEvent) {

      var bankAccountId = bankAccountOpenedEvent.bankAccountId().value();
      log.info(
        "Publishing BankAccountOpenedEvent to Kafka [topic={}, key={}]", topicProperties.topicName(), bankAccountId
      );

//      var producerRecord = new ProducerRecord<String, Object>(
//        topicProperties.topicName(),
//        bankAccountId.toString(),
//        messageMapper.toIntegrationEvent(bankAccountOpenedEvent, UUID.randomUUID().toString())
//      );
//
//      kafkaTemplate.send(producerRecord);
    }
  }
}
