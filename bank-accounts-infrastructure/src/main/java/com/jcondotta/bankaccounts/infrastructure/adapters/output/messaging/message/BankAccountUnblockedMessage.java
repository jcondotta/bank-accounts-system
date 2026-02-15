package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message;

import java.util.UUID;

public record BankAccountUnblockedMessage(UUID bankAccountId, String occurredAt) {
}