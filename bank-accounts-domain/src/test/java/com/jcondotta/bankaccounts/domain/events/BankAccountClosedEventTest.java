package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.events.types.DomainEventType;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.EventId;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountClosedEventTest {

  private static final EventId EVENT_ID = EventId.newId();
  private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();
  private static final Clock FIXED_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final ZonedDateTime OCCURRED_AT = ZonedDateTime.now(FIXED_CLOCK);

  @Test
  void shouldCreateBankAccountClosedEvent_whenAllArgumentsAreValid() {
    BankAccountClosedEvent event =
      new BankAccountClosedEvent(EVENT_ID, BANK_ACCOUNT_ID, OCCURRED_AT);

    assertThat(event.eventId()).isEqualTo(EVENT_ID);
    assertThat(event.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID);
    assertThat(event.occurredAt()).isEqualTo(OCCURRED_AT);
    assertThat(event.eventType())
      .isEqualTo(DomainEventType.BANK_ACCOUNT_CLOSED);
    assertThat(event.eventType().value())
      .isEqualTo("BankAccountClosed");
  }

  @Test
  void shouldThrowNullPointerException_whenEventIdIsNull() {
    assertThatThrownBy(() ->
      new BankAccountClosedEvent(null, BANK_ACCOUNT_ID, OCCURRED_AT)
    ).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldThrowNullPointerException_whenBankAccountIdIsNull() {
    assertThatThrownBy(() ->
      new BankAccountClosedEvent(EVENT_ID, null, OCCURRED_AT)
    ).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldThrowNullPointerException_whenOccurredAtIsNull() {
    assertThatThrownBy(() ->
      new BankAccountClosedEvent(EVENT_ID, BANK_ACCOUNT_ID, null)
    ).isInstanceOf(NullPointerException.class);
  }
}