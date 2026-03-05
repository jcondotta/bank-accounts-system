package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.domain.identity.EventId;
import com.jcondotta.domain.exception.DomainValidationException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountJointHolderAddedEventTest {

  private static final EventId EVENT_ID = EventId.newId();
  private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();
  private static final AccountHolderId ACCOUNT_HOLDER_ID = AccountHolderId.newId();
  private static final Clock FIXED_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final Instant OCCURRED_AT = Instant.now(FIXED_CLOCK);

  @Test
  void shouldCreateJointAccountHolderAddedEvent_whenAllArgumentsAreValid() {
    var event = new BankAccountJointHolderAddedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        ACCOUNT_HOLDER_ID,
        OCCURRED_AT
      );

    assertThat(event.eventId()).isEqualTo(EVENT_ID);
    assertThat(event.aggregateId()).isEqualTo(BANK_ACCOUNT_ID);
    assertThat(event.accountHolderId()).isEqualTo(ACCOUNT_HOLDER_ID);
    assertThat(event.occurredAt()).isEqualTo(OCCURRED_AT);
  }

  @Test
  void shouldThrowException_whenEventIdIsNull() {
    assertThatThrownBy(() ->
      new BankAccountJointHolderAddedEvent(
        null,
        BANK_ACCOUNT_ID,
        ACCOUNT_HOLDER_ID,
        OCCURRED_AT
      )
    ).isInstanceOf(DomainValidationException.class);
  }

  @Test
  void shouldThrowException_whenBankAccountIdIsNull() {
    assertThatThrownBy(() ->
      new BankAccountJointHolderAddedEvent(
        EVENT_ID,
        null,
        ACCOUNT_HOLDER_ID,
        OCCURRED_AT
      )
    ).isInstanceOf(DomainValidationException.class);
  }

  @Test
  void shouldThrowException_whenAccountHolderIdIsNull() {
    assertThatThrownBy(() ->
      new BankAccountJointHolderAddedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        null,
        OCCURRED_AT
      )
    ).isInstanceOf(DomainValidationException.class);
  }

  @Test
  void shouldThrowException_whenOccurredAtIsNull() {
    assertThatThrownBy(() ->
      new BankAccountJointHolderAddedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        ACCOUNT_HOLDER_ID,
        null
      )
    ).isInstanceOf(DomainValidationException.class);
  }
}
