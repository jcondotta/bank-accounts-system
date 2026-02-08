package com.jcondotta.bankaccounts.application.usecase.openbankaccount.model;

import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

import java.time.ZonedDateTime;

public record OpenBankAccountResult(BankAccountId bankAccountId, ZonedDateTime createdAt) {
}