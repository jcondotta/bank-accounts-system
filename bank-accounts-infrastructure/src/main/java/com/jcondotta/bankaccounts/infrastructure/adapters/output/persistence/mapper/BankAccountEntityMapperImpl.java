package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.mapper;

import com.jcondotta.bankaccounts.domain.entities.AccountHolder;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankAccountEntityKey;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankingEntity;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.enums.EntityType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class BankAccountEntityMapperImpl implements BankAccountEntityMapper {

  private final AccountHolderEntityMapper accountHolderEntityMapper;

  @Override
  public List<BankingEntity> toBankingEntities(BankAccount bankAccount) {
    var bankAccountEntity = toEntity(bankAccount);
    var accountHolderEntities = bankAccount.getAccountHolders().stream()
      .map(accountHolder -> accountHolderEntityMapper.toAccountHolderEntity(bankAccount.getBankAccountId(), accountHolder))
      .toList();

    return Stream.concat(
      Stream.of(bankAccountEntity),
      accountHolderEntities.stream()
    ).toList();
  }


  BankingEntity toEntity(BankAccount bankAccount) {
    return BankingEntity.builder()
      .partitionKey(BankAccountEntityKey.partitionKey(bankAccount.getBankAccountId()))
      .sortKey(BankAccountEntityKey.sortKey(bankAccount.getBankAccountId()))
      .entityType(EntityType.BANK_ACCOUNT)
      .bankAccountId(bankAccount.getBankAccountId().value())
      .accountType(bankAccount.getAccountType())
      .currency(bankAccount.getCurrency())
      .iban(bankAccount.getIban().value())
      .status(bankAccount.getStatus())
      .createdAt(bankAccount.getCreatedAt().toInstant())
      .createdAtZone(bankAccount.getCreatedAt().getZone())
      .build();
  }

  @Override
  public BankAccount toDomain(BankingEntity entity, List<BankingEntity> accountHolderEntities) {
    List<AccountHolder> accountHolders = accountHolderEntities.stream()
      .map(accountHolderEntityMapper::toDomain)
      .toList();

    return BankAccount.restore(
      BankAccountId.of(entity.getBankAccountId()),
      entity.getAccountType(),
      entity.getCurrency(),
      Iban.of(entity.getIban()),
      entity.getStatus(),
      ZonedDateTime.ofInstant(entity.getCreatedAt(), entity.getCreatedAtZone()),
      accountHolders
    );
  }
}