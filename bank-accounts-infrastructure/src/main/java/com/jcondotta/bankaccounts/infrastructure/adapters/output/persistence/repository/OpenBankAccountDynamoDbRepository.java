package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.repository;

import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.OpenBankAccountRepository;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.outbox.OutboxEventCollector;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankingEntity;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.OutboxEntity;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.mapper.BankAccountEntityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

@Slf4j
@Repository
@AllArgsConstructor
public class OpenBankAccountDynamoDbRepository implements OpenBankAccountRepository {

  private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
  private final DynamoDbTable<BankingEntity> bankingTable;
  private final DynamoDbTable<OutboxEntity> outboxTable;
  private final BankAccountEntityMapper bankAccountEntityMapper;
  private final OutboxEventCollector outboxEventCollector;

  @Override
  public void create(BankAccount bankAccount) {
    TransactWriteItemsEnhancedRequest.Builder builder = TransactWriteItemsEnhancedRequest.builder();

    bankAccountEntityMapper.toBankingEntities(bankAccount)
      .forEach(bankingEntity -> builder
        .addPutItem(bankingTable, bankingEntity)
      );

    outboxEventCollector.collect(bankAccount)
      .forEach(entry ->
        builder.addPutItem(outboxTable, entry)
      );

    dynamoDbEnhancedClient.transactWriteItems(builder.build());
  }
}