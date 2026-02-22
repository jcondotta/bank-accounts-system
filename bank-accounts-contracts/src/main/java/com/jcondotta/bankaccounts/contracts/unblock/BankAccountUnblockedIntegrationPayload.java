package com.jcondotta.bankaccounts.contracts.unblock;

import java.util.UUID;

public record BankAccountUnblockedIntegrationPayload(UUID bankAccountId) {
}
