package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.repository;

import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.LookupBankAccountRepository;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankAccountEntityKey;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankingEntity;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.mapper.BankAccountEntityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class LookupBankAccountDynamoDbRepository implements LookupBankAccountRepository {

  private final DynamoDbTable<BankingEntity> bankingEntityDynamoDbTable;
  private final BankAccountEntityMapper bankAccountEntityMapper;

  @Override
  public Optional<BankAccount> byId(BankAccountId bankAccountId) {
    var partitionKey = BankAccountEntityKey.partitionKey(bankAccountId);
    var queryConditional = QueryConditional.keyEqualTo(Key.builder()
      .partitionValue(partitionKey)
      .build());

    log.atInfo()
      .setMessage("Querying bank account and account holders for bankAccountId: {}")
      .addArgument(bankAccountId)
      .log();

    var bankingEntities = bankingEntityDynamoDbTable.query(queryConditional)
      .items().stream()
      .toList();

    return Optional.ofNullable(findBankAccountEntity(bankingEntities))
      .map(bankAccountEntity -> {
        var accountHolderEntities = findAccountHolderEntities(bankingEntities);
        return bankAccountEntityMapper.toDomain(bankAccountEntity, accountHolderEntities);
      });
  }

  private BankingEntity findBankAccountEntity(List<BankingEntity> items) {
    return items.stream()
      .filter(BankingEntity::isEntityTypeBankAccount)
      .findFirst()
      .orElseThrow(() ->
        new BankAccountNotFoundException(BankAccountId.newId())
      );

  }

  private List<BankingEntity> findAccountHolderEntities(List<BankingEntity> items) {
    return items.stream()
      .filter(BankingEntity::isEntityTypeAccountHolder)
      .toList();
  }
}