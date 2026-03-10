package com.jcondotta.banking.recipients.infrastructure.bankaccount.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka.topics.bank-account-activated")
public record BankAccountActivatedTopicProperties(String topicName) {

}