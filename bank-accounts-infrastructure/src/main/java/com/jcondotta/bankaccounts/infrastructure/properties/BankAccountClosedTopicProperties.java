package com.jcondotta.bankaccounts.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka.topics.bank-account-closed")
public record BankAccountClosedTopicProperties(String topicName) {}