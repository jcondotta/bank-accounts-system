package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.kafka;

import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountUnblockedEventPublisher;
import com.jcondotta.bankaccounts.domain.events.BankAccountUnblockedEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper.BankAccountUnblockedIntegrationEventMapper;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountUnblockedTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBankAccountUnblockedEventPublisher implements BankAccountUnblockedEventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final BankAccountUnblockedIntegrationEventMapper messageMapper;
  private final BankAccountUnblockedTopicProperties topicProperties;

  @Override
  public void publish(DomainEvent event) {
    if (event instanceof BankAccountUnblockedEvent bankAccountUnblockedEvent) {

      log.info(
        "Publishing BankAccountUnblockedEvent to Kafka [topic={}, key={}]",
        topicProperties.topicName(),
        bankAccountUnblockedEvent.bankAccountId()
      );

//      var producerRecord = new ProducerRecord<String, Object>(
//        topicProperties.topicName(),
//        bankAccountUnblockedEvent.bankAccountId().value().toString(),
//        messageMapper.toMessage(bankAccountUnblockedEvent)
//      );
//
//      kafkaTemplate.send(producerRecord);
    }
  }
}