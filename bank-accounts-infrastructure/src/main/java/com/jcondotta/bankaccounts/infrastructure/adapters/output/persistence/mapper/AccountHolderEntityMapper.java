package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.mapper;

import com.jcondotta.bankaccounts.domain.entities.AccountHolder;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.AccountHolderEntityKey;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankingEntity;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.enums.EntityType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountHolderEntityMapper {

  default BankingEntity toAccountHolderEntity(BankAccountId bankAccountId, AccountHolder accountHolder) {
    return BankingEntity.builder()
      .partitionKey(AccountHolderEntityKey.partitionKey(bankAccountId))
      .sortKey(AccountHolderEntityKey.sortKey(accountHolder.id()))
      .entityType(EntityType.ACCOUNT_HOLDER)
      .bankAccountId(bankAccountId.value())
      .accountHolderId(accountHolder.id().value())
      .accountHolderName(accountHolder.name().value())
      .passportNumber(accountHolder.passportNumber().value())
      .dateOfBirth(accountHolder.dateOfBirth().value())
      .email(accountHolder.email().value())
      .accountHolderType(accountHolder.accountHolderType())
      .createdAt(accountHolder.createdAt())
      .build();
  }

  default AccountHolder toDomain(BankingEntity accountHolderEntity) {
    return BankAccount.restoreAccountHolder(
      AccountHolderId.of(accountHolderEntity.getAccountHolderId()),
      AccountHolderName.of(accountHolderEntity.getAccountHolderName()),
      PassportNumber.of(accountHolderEntity.getPassportNumber()),
      DateOfBirth.of(accountHolderEntity.getDateOfBirth()),
      Email.of(accountHolderEntity.getEmail()),
      accountHolderEntity.getAccountHolderType(),
      accountHolderEntity.getCreatedAt()
    );
  }
}