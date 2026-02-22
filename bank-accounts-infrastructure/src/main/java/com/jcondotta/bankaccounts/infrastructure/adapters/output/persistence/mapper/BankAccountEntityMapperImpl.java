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

import java.util.List;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class BankAccountEntityMapperImpl implements BankAccountEntityMapper {

  private final AccountHolderEntityMapper accountHolderEntityMapper;

  @Override
  public List<BankingEntity> toBankingEntities(BankAccount bankAccount) {
    var bankAccountEntity = toEntity(bankAccount);
    var accountHolderEntities = bankAccount.accountHolders().stream()
      .map(accountHolder -> accountHolderEntityMapper.toAccountHolderEntity(bankAccount.id(), accountHolder))
      .toList();

    return Stream.concat(
      Stream.of(bankAccountEntity),
      accountHolderEntities.stream()
    ).toList();
  }


  BankingEntity toEntity(BankAccount bankAccount) {
    return BankingEntity.builder()
      .partitionKey(BankAccountEntityKey.partitionKey(bankAccount.id()))
      .sortKey(BankAccountEntityKey.sortKey(bankAccount.id()))
      .entityType(EntityType.BANK_ACCOUNT)
      .bankAccountId(bankAccount.id().value())
      .accountType(bankAccount.accountType())
      .currency(bankAccount.currency())
      .iban(bankAccount.iban().value())
      .status(bankAccount.accountStatus())
      .createdAt(bankAccount.createdAt())
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
      entity.getCreatedAt(),
      accountHolders
    );
  }
}