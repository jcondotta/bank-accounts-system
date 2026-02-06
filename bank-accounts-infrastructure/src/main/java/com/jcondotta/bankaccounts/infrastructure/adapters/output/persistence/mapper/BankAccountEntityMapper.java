package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.mapper;

import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity.BankingEntity;

import java.util.List;

public interface BankAccountEntityMapper {

    List<BankingEntity> toBankingEntities(BankAccount bankAccount);

    BankAccount toDomain(BankingEntity entity, List<BankingEntity> holderEntities);
}