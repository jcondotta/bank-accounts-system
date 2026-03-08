package com.jcondotta.banking.recipients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.jcondotta.banking.recipients")
@ConfigurationPropertiesScan(basePackages = "com.jcondotta.banking.recipients.infrastructure.bankaccount.properties")
public class RecipientApplication {

  public static void main(String[] args) {
    SpringApplication.run(RecipientApplication.class, args);
  }
}

