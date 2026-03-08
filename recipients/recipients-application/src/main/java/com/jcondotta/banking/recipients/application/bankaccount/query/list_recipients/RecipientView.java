package com.jcondotta.banking.recipients.application.bankaccount.query.list_recipients;

import com.jcondotta.banking.recipients.domain.recipient.identity.RecipientId;
import com.jcondotta.banking.recipients.domain.recipient.value_objects.Iban;
import com.jcondotta.banking.recipients.domain.recipient.value_objects.RecipientName;

import java.time.Instant;

public record RecipientView(
  RecipientId recipientId,
  RecipientName recipientName,
  Iban iban,
  Instant createdAt
) {}