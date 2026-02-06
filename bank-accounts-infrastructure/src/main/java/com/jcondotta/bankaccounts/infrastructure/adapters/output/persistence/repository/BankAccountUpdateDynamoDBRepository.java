package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.repository;

import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.updatebankaccount.BankAccountUpdateRepository;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class BankAccountUpdateDynamoDBRepository implements BankAccountUpdateRepository {

//    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateBankAccountDynamoDBRepository.class);

//    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
//    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
//    private final BankAccountEntityMapper bankAccountEntityMapper;

//    @Override
//    public void update(BankAccount bankAccount) {
//        LOGGER.info("Updating bank account and holders for ID: {}", bankAccount.bankAccountId());
//
//        // Novas entidades geradas a partir do estado atual no domínio
//        var newEntities = bankAccountEntityMapper.toEntities(bankAccount);
//
//        // Obtem os SKs das novas entidades (normalmente: BANK_ACCOUNT, ACCOUNT_HOLDER#xxx, etc.)
//        Set<String> currentSkSet = newEntities.stream()
//            .map(BankingEntity::getSortKey) // ou getSk()
//            .collect(Collectors.toSet());
//
//        // Consulta todos os registros atuais no DynamoDB para a PK da conta
//        List<BankingEntity> existingEntities = bankingEntityDynamoDbTable
//            .query(r -> r.queryConditional(QueryConditional.keyEqualTo(
//                Key.builder().partitionValue(BankAccountEntityKey.partitionKey(bankAccount.bankAccountId())).build()
//            )))
//            .items()
//            .stream()
//            .toList();
//
//        // Determina quais SKs devem ser removidos
//        List<BankingEntity> toDelete = existingEntities.stream()
//            .filter(entity -> !currentSkSet.contains(entity.getSortKey()))
//            .toList();
//
//        // Monta transação com put + delete
//        var txBuilder = TransactWriteItemsEnhancedRequest.builder();
//
//        newEntities.forEach(entity ->
//            txBuilder.addPutItem(bankingEntityDynamoDbTable, entity)
//        );
//
//        toDelete.forEach(entity ->
//            txBuilder.addDeleteItem(bankingEntityDynamoDbTable,
//                Key.builder()
//                    .partitionValue(entity.getPartitionKey())
//                    .sortValue(entity.getSortKey())
//                    .build()
//            )
//        );
//
//        // Executa a transação
//        dynamoDbEnhancedClient.transactWriteItems(txBuilder.build());
//
//        LOGGER.debug("Successfully updated bank account and holders for ID: {}", bankAccount.bankAccountId());
//    }

    @Override
    public void save(BankAccount bankAccount) {

    }
}