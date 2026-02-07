package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging;

import java.util.UUID;

public record JointAccountHolderAddedMessage(
  UUID eventId,
  UUID bankAccountId,
  UUID accountHolderId,
  String name,
  String passportNumber,
  String dateOfBirth,
  String occurredAt
) {
}
