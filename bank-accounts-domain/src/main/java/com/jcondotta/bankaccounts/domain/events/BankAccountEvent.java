package com.jcondotta.bankaccounts.domain.events;

import java.time.ZonedDateTime;

public sealed interface BankAccountEvent
  extends DomainEvent
  permits BankAccountOpenedEvent, BankAccountActivatedEvent, BankAccountBlockedEvent, JointAccountHolderAddedEvent {

  ZonedDateTime occurredAt();
}