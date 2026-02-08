package com.jcondotta.bankaccounts.domain.events;

/**
 * Sealed interface representing all events related to bank accounts.
 * This allows for a closed set of event types, ensuring that only specific events can be emitted.
 */
public sealed interface BankAccountEvent
  extends DomainEvent
  permits BankAccountOpenedEvent, BankAccountActivatedEvent, BankAccountBlockedEvent, JointAccountHolderAddedEvent {
}