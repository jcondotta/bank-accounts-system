package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.validation.AccountHolderValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.BankAccountValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.DomainEventValidationErrors;
import com.jcondotta.bankaccounts.domain.validation.DomainValidationErrors;
import com.jcondotta.bankaccounts.domain.value_objects.*;

import java.time.ZonedDateTime;

import static java.util.Objects.requireNonNull;

public record JointAccountHolderAddedEvent(
  EventId eventId,
  BankAccountId bankAccountId,
  AccountHolderId accountHolderId,
  AccountHolderName name,
  PassportNumber passportNumber,
  DateOfBirth dateOfBirth,
  ZonedDateTime occurredAt
) implements BankAccountEvent {

  public JointAccountHolderAddedEvent {
    requireNonNull(eventId, DomainEventValidationErrors.EVENT_ID_NOT_NULL);
    requireNonNull(bankAccountId, BankAccountValidationErrors.ID_NOT_NULL);
    requireNonNull(accountHolderId, AccountHolderValidationErrors.ID_NOT_NULL);
    requireNonNull(name, AccountHolderValidationErrors.NAME_NOT_NULL);
    requireNonNull(passportNumber, AccountHolderValidationErrors.PASSPORT_NUMBER_NOT_NULL);
    requireNonNull(dateOfBirth, AccountHolderValidationErrors.DATE_OF_BIRTH_NOT_NULL);
    requireNonNull(occurredAt, DomainEventValidationErrors.EVENT_OCCURRED_AT_NOT_NULL);
  }
}
