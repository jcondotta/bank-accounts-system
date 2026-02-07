package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging;

import java.util.UUID;

public record BankAccountOpenedMessage(
  UUID eventId,
  UUID bankAccountId,
  String accountType,
  String currency,
  String iban,
  String status,
  String occurredAt
) {
}