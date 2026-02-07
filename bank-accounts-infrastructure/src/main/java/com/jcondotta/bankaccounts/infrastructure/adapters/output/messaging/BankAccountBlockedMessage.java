package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging;

import java.time.ZonedDateTime;
import java.util.UUID;

public record BankAccountBlockedMessage(UUID eventId, UUID bankAccountId, String occurredAt) {
}