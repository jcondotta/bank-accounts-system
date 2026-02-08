package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message;

import java.util.UUID;

public record BankAccountActivatedMessage(UUID bankAccountId, String occurredAt) {
}