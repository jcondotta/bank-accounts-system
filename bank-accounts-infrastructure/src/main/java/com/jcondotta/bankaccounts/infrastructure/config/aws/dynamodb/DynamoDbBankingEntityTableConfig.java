package com.jcondotta.bankaccounts.infrastructure.config.aws.dynamodb;

import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankingEntity;
import com.jcondotta.bankaccounts.infrastructure.properties.BankingEntityTableProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class DynamoDbBankingEntityTableConfig {

  @Bean
  public DynamoDbTable<BankingEntity> dynamoDbTable(DynamoDbEnhancedClient dynamoDbEnhancedClient, BankingEntityTableProperties tableProperties) {
    return dynamoDbEnhancedClient.table(
        tableProperties.tableName(),
        TableSchema.fromBean(BankingEntity.class)
    );
  }
}
