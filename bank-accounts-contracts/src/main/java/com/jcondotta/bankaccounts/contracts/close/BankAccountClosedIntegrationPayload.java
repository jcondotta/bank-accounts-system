package com.jcondotta.bankaccounts.contracts.close;

import java.util.UUID;

public record BankAccountClosedIntegrationPayload(UUID bankAccountId) {
}
