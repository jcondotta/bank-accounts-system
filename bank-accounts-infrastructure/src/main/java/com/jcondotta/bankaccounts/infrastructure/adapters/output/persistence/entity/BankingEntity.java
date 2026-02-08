package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.enums.EntityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Setter
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BankingEntity {

  private String partitionKey;
  private String sortKey;
  private EntityType entityType;

  private UUID bankAccountId;
  private AccountType accountType;
  private Currency currency;
  private String iban;
  private AccountStatus status;

  private UUID accountHolderId;
  private String accountHolderName;
  private String passportNumber;
  private LocalDate dateOfBirth;
  private AccountHolderType accountHolderType;

  private Instant createdAt;
  private ZoneId createdAtZone;

  @DynamoDbPartitionKey
  @DynamoDbAttribute("partitionKey")
  public String getPartitionKey() {
    return partitionKey;
  }

  @DynamoDbSortKey
  @DynamoDbAttribute("sortKey")
  public String getSortKey() {
    return sortKey;
  }

  @DynamoDbAttribute("entityType")
  public EntityType getEntityType() {
    return entityType;
  }

  @DynamoDbAttribute("bankAccountId")
  public UUID getBankAccountId() {
    return bankAccountId;
  }

  @DynamoDbAttribute("accountType")
  public AccountType getAccountType() {
    return accountType;
  }

  @DynamoDbAttribute("currency")
  public Currency getCurrency() {
    return currency;
  }

  @DynamoDbAttribute("iban")
  public String getIban() {
    return iban;
  }

  @DynamoDbAttribute("accountStatus")
  public AccountStatus getStatus() {
    return status;
  }

  @DynamoDbAttribute("primaryAccountHolderId")
  public UUID getAccountHolderId() {
    return accountHolderId;
  }

  @DynamoDbAttribute("accountHolderName")
  public String getAccountHolderName() {
    return accountHolderName;
  }

  @DynamoDbAttribute("passportNumber")
  public String getPassportNumber() {
    return passportNumber;
  }

  @DynamoDbAttribute("dateOfBirth")
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  @DynamoDbAttribute("accountHolderType")
  public AccountHolderType getAccountHolderType() {
    return accountHolderType;
  }

  @DynamoDbAttribute("createdAt")
  public Instant getCreatedAt() {
    return createdAt;
  }

  @DynamoDbAttribute("createdAtZone")
  public ZoneId getCreatedAtZone() {
    return createdAtZone;
  }

  public boolean isEntityTypeBankAccount() {
    return entityType == EntityType.BANK_ACCOUNT;
  }

  public boolean isEntityTypeAccountHolder() {
    return entityType == EntityType.ACCOUNT_HOLDER;
  }
}