package com.jcondotta.bankaccounts.application.command.open.model;

import com.jcondotta.banking.accounts.domain.bankaccount.identity.BankAccountId;

import java.time.Instant;

public record OpenBankAccountResult(BankAccountId bankAccountId, Instant createdAt) {
}