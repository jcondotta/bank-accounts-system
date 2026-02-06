package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.mapper;

import com.jcondotta.bankaccounts.domain.entities.AccountHolder;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.AccountHolderEntityKey;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankingEntity;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.enums.EntityType;
import org.mapstruct.Mapper;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface AccountHolderEntityMapper {

  default BankingEntity toAccountHolderEntity(BankAccountId bankAccountId, AccountHolder accountHolder) {
    return BankingEntity.builder()
      .partitionKey(AccountHolderEntityKey.partitionKey(bankAccountId))
      .sortKey(AccountHolderEntityKey.sortKey(accountHolder.getAccountHolderId()))
      .entityType(EntityType.ACCOUNT_HOLDER)
      .bankAccountId(bankAccountId.value())
      .accountHolderId(accountHolder.getAccountHolderId().value())
      .accountHolderName(accountHolder.getAccountHolderName().value())
      .passportNumber(accountHolder.getPassportNumber().value())
      .dateOfBirth(accountHolder.getDateOfBirth().value())
      .accountHolderType(accountHolder.getAccountHolderType())
      .createdAt(accountHolder.getCreatedAt().toInstant())
      .createdAtZone(accountHolder.getCreatedAt().getZone())
      .build();
  }

  default AccountHolder toDomain(BankingEntity accountHolderEntity) {
    return BankAccount.restoreAccountHolder(
      AccountHolderId.of(accountHolderEntity.getAccountHolderId()),
      AccountHolderName.of(accountHolderEntity.getAccountHolderName()),
      PassportNumber.of(accountHolderEntity.getPassportNumber()),
      DateOfBirth.of(accountHolderEntity.getDateOfBirth()),
      accountHolderEntity.getAccountHolderType(),
      ZonedDateTime.ofInstant(accountHolderEntity.getCreatedAt(), accountHolderEntity.getCreatedAtZone())
    );
  }
}