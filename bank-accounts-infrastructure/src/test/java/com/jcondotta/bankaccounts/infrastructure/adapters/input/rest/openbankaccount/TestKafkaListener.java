package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount;

import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.common.EventEnvelope;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message.BankAccountOpenedMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class TestKafkaListener {

  private final BlockingQueue<ConsumerRecord<String, EventEnvelope<BankAccountOpenedMessage>>> records =
      new LinkedBlockingQueue<>();

  @KafkaListener(
      topics = "${KAFKA_BANK_ACCOUNT_OPENED_TOPIC_NAME}",
      groupId = "test-bank-account-opened-consumer"
  )
  public void listen(ConsumerRecord<String, EventEnvelope<BankAccountOpenedMessage>> record) {
    records.add(record);
  }

  public ConsumerRecord<String, EventEnvelope<BankAccountOpenedMessage>> poll(long timeout, TimeUnit unit)
      throws InterruptedException {
    return records.poll(timeout, unit);
  }

  public void clear() {
    records.clear();
  }
}
