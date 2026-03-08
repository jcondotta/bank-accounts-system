package com.jcondotta.banking.recipients.infrastructure.bankaccount.adapters.output.persistence.mapper;

import com.jcondotta.banking.recipients.domain.recipient.aggregate.BankAccount;
import com.jcondotta.banking.recipients.domain.recipient.aggregate.Recipients;
import com.jcondotta.banking.recipients.domain.recipient.enums.AccountStatus;
import com.jcondotta.banking.recipients.domain.recipient.identity.BankAccountId;
import com.jcondotta.banking.recipients.infrastructure.bankaccount.adapters.output.persistence.entity.BankAccountEntityKey;
import com.jcondotta.banking.recipients.infrastructure.bankaccount.adapters.output.persistence.entity.BankingEntity;
import com.jcondotta.banking.recipients.infrastructure.bankaccount.adapters.output.persistence.enums.EntityType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class BankAccountEntityMapperImpl implements BankAccountEntityMapper {

  private final RecipientEntityMapper recipientEntityMapper;

  @Override
  public List<BankingEntity> toEntities(BankAccount bankAccount) {
    var bankAccountEntity = toEntity(bankAccount);
    var recipientEntities = bankAccount.getRecipients().stream()
      .map(recipient -> recipientEntityMapper.toEntity(bankAccount.getId(), recipient))
      .toList();

    return Stream.concat(
      Stream.of(bankAccountEntity),
      recipientEntities.stream()
    ).toList();
  }

  BankingEntity toEntity(BankAccount bankAccount) {
    return BankingEntity.builder()
      .partitionKey(BankAccountEntityKey.partitionKey(bankAccount.getId()))
      .sortKey(BankAccountEntityKey.sortKey(bankAccount.getId()))
      .entityType(EntityType.BANK_ACCOUNT)
      .bankAccountId(bankAccount.getId().value())
      .accountStatus(bankAccount.getAccountStatus().name())
      .createdAt(Instant.now())
      .build();
  }

  @Override
  public BankAccount restore(List<BankingEntity> bankingEntities) {
    var bankAccountEntity = findBankAccountEntity(bankingEntities)
      .orElseThrow(() -> new IllegalStateException("Bank account entity not found"));

    var recipientEntities = bankingEntities.stream()
      .filter(BankingEntity::isRecipient)
      .map(recipientEntityMapper::toDomain)
      .toList();

    return BankAccount.restore(
      BankAccountId.of(bankAccountEntity.getBankAccountId()),
      AccountStatus.valueOf(bankAccountEntity.getAccountStatus()),
      Recipients.of(recipientEntities)
    );
  }

  private Optional<BankingEntity> findBankAccountEntity(List<BankingEntity> entities) {
    return entities.stream()
      .filter(BankingEntity::isBankAccount)
      .findFirst();
  }
}