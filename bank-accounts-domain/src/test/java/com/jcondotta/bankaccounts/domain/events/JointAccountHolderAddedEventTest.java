package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.events.types.DomainEventType;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.EventId;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JointAccountHolderAddedEventTest {

  private static final EventId EVENT_ID = EventId.newId();
  private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();
  private static final AccountHolderId ACCOUNT_HOLDER_ID = AccountHolderId.newId();
  private static final Clock FIXED_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final Instant OCCURRED_AT = Instant.now(FIXED_CLOCK);

  @Test
  void shouldCreateJointAccountHolderAddedEvent_whenAllArgumentsAreValid() {
    JointAccountHolderAddedEvent event =
      new JointAccountHolderAddedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        ACCOUNT_HOLDER_ID,
        OCCURRED_AT
      );

    assertThat(event.eventId()).isEqualTo(EVENT_ID);
    assertThat(event.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID);
    assertThat(event.accountHolderId()).isEqualTo(ACCOUNT_HOLDER_ID);
    assertThat(event.occurredAt()).isEqualTo(OCCURRED_AT);
    assertThat(event.eventType())
      .isEqualTo(DomainEventType.JOINT_ACCOUNT_HOLDER_ADDED);
  }

  @Test
  void shouldThrowNullPointerException_whenEventIdIsNull() {
    assertThatThrownBy(() ->
      new JointAccountHolderAddedEvent(
        null,
        BANK_ACCOUNT_ID,
        ACCOUNT_HOLDER_ID,
        OCCURRED_AT
      )
    ).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldThrowNullPointerException_whenBankAccountIdIsNull() {
    assertThatThrownBy(() ->
      new JointAccountHolderAddedEvent(
        EVENT_ID,
        null,
        ACCOUNT_HOLDER_ID,
        OCCURRED_AT
      )
    ).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldThrowNullPointerException_whenAccountHolderIdIsNull() {
    assertThatThrownBy(() ->
      new JointAccountHolderAddedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        null,
        OCCURRED_AT
      )
    ).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldThrowNullPointerException_whenOccurredAtIsNull() {
    assertThatThrownBy(() ->
      new JointAccountHolderAddedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        ACCOUNT_HOLDER_ID,
        null
      )
    ).isInstanceOf(NullPointerException.class);
  }
}
