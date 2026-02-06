package com.jcondotta.bankaccounts.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.aws.dynamodb.tables.banking-entities")
public record BankingEntityTableProperties(String tableName) {
}
