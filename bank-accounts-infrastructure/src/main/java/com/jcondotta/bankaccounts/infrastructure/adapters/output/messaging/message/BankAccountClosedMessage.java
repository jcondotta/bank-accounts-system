package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message;

import java.util.UUID;

public record BankAccountClosedMessage(UUID bankAccountId, String occurredAt) {
}