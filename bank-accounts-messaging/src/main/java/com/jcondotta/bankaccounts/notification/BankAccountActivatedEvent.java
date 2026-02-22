package com.jcondotta.bankaccounts.notification;

import java.util.UUID;

public record BankAccountActivatedEvent(UUID bankAccountId, String occurredAt) {
}