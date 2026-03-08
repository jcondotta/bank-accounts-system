package com.jcondotta.banking.recipients.infrastructure.bankaccount.adapters.output.persistence.mapper;

import com.jcondotta.banking.recipients.domain.recipient.aggregate.BankAccount;
import com.jcondotta.banking.recipients.infrastructure.bankaccount.adapters.output.persistence.entity.BankingEntity;

import java.util.List;

public interface BankAccountEntityMapper {

  List<BankingEntity> toEntities(BankAccount bankAccount);

  BankAccount restore(List<BankingEntity> bankingEntities);
}