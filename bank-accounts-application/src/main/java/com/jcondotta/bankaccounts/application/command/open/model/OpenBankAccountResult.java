package com.jcondotta.bankaccounts.application.command.open.model;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.time.Instant;

public record OpenBankAccountResult(BankAccountId bankAccountId, Instant createdAt) {
}