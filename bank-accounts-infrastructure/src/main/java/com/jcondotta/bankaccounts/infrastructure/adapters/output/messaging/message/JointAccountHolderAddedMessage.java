package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message;

import java.util.UUID;

public record JointAccountHolderAddedMessage(UUID bankAccountId, UUID accountHolderId, String occurredAt) {
}
