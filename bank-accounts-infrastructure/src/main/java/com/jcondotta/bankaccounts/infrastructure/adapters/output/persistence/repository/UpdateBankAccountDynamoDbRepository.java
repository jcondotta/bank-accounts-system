package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.repository;

import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.UpdateBankAccountRepository;
import com.jcondotta.bankaccounts.domain.aggregates.BankAccount;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.outbox.OutboxEventCollector;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankAccountEntityKey;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankingEntity;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.OutboxEntity;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.mapper.BankAccountEntityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class UpdateBankAccountDynamoDbRepository implements UpdateBankAccountRepository {

//    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateBankAccountDynamoDBRepository.class);

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
    private final DynamoDbTable<OutboxEntity> outboxTable;
    private final BankAccountEntityMapper bankAccountEntityMapper;
    private final OutboxEventCollector outboxEventCollector;

    @Override
    public void update(BankAccount bankAccount) {
        log.info("Updating bank account and holders for ID: {}", bankAccount.id());

        // Novas entidades geradas a partir do estado atual no domínio
        var newEntities = bankAccountEntityMapper.toBankingEntities(bankAccount);

        // Obtem os SKs das novas entidades (normalmente: BANK_ACCOUNT, ACCOUNT_HOLDER#xxx, etc.)
        Set<String> currentSkSet = newEntities.stream()
            .map(BankingEntity::getSortKey) // ou getSk()
            .collect(Collectors.toSet());

        // Consulta todos os registros atuais no DynamoDB para a PK da conta
        List<BankingEntity> existingEntities = bankingEntityDynamoDbTable
            .query(r -> r.queryConditional(QueryConditional.keyEqualTo(
                Key.builder().partitionValue(BankAccountEntityKey.partitionKey(bankAccount.id())).build()
            )))
            .items()
            .stream()
            .filter(entity -> !"OUTBOX_EVENT".equals(entity.getEntityType()))
            .toList();

        // Determina quais SKs devem ser removidos
        List<BankingEntity> toDelete = existingEntities.stream()
            .filter(entity -> !currentSkSet.contains(entity.getSortKey()))
            .toList();

        // Monta transação com put + delete
        var txBuilder = TransactWriteItemsEnhancedRequest.builder();

        newEntities.forEach(entity ->
            txBuilder.addPutItem(bankingEntityDynamoDbTable, entity)
        );

        outboxEventCollector.collect(bankAccount)
          .forEach(entry ->
            txBuilder.addPutItem(outboxTable, entry)
          );

        toDelete.forEach(entity ->
            txBuilder.addDeleteItem(bankingEntityDynamoDbTable,
                Key.builder()
                    .partitionValue(entity.getPartitionKey())
                    .sortValue(entity.getSortKey())
                    .build()
            )
        );

        // Executa a transação
        dynamoDbEnhancedClient.transactWriteItems(txBuilder.build());

        log.debug("Successfully updated bank account and holders for ID: {}", bankAccount.id());
    }
}