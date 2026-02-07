package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.repository;

import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.OpenBankAccountRepository;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankingEntity;
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
    private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
    private final BankAccountEntityMapper bankAccountEntityMapper;

    @Override
    public void create(BankAccount bankAccount) {
        log.atInfo()
            .setMessage("Starting transaction to save bank account.")
            .log();

        TransactWriteItemsEnhancedRequest.Builder builder = TransactWriteItemsEnhancedRequest.builder();
        bankAccountEntityMapper.toBankingEntities(bankAccount)
            .forEach(bankingEntity -> builder
                .addPutItem(bankingEntityDynamoDbTable, bankingEntity)
            );

        dynamoDbEnhancedClient.transactWriteItems(builder.build());

        log.atInfo()
            .setMessage("Bank account and primary account holder created successfully.")
            .log();
    }
}