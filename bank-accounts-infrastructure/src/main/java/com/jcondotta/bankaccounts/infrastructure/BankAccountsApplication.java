package com.jcondotta.bankaccounts.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.jcondotta.bankaccounts")
@EnableFeignClients(basePackages = "com.jcondotta.bankaccounts.infrastructure")
@ConfigurationPropertiesScan(basePackages = "com.jcondotta.bankaccounts.infrastructure.properties")
public class BankAccountsApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankAccountsApplication.class, args);
  }

}
