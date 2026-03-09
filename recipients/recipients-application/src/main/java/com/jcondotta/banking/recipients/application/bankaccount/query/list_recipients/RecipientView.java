package com.jcondotta.banking.recipients.application.bankaccount.query.list_recipients;

import java.time.Instant;
import java.util.UUID;

public record RecipientView(
  UUID recipientId,
  String recipientName,
  String iban,
  Instant createdAt
) {}