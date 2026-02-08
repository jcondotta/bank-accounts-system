package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.message;

import java.util.UUID;

public record BankAccountOpenedMessage(
  UUID bankAccountId,
  String accountType,
  String currency,
  UUID primaryAccountHolderId,
  String occurredAt
) {
}