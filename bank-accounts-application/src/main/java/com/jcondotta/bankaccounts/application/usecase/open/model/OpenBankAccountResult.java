package com.jcondotta.bankaccounts.application.usecase.open.model;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.time.ZonedDateTime;

public record OpenBankAccountResult(BankAccountId bankAccountId, ZonedDateTime createdAt) {
}