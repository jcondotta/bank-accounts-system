package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message;

import java.util.UUID;

public record BankAccountBlockedMessage(UUID bankAccountId, String occurredAt) {
}