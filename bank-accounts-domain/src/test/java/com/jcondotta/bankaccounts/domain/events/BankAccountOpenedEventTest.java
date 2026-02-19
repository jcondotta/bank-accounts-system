package com.jcondotta.bankaccounts.domain.events;

import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.types.DomainEventType;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.EventId;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountOpenedEventTest {

  private static final EventId EVENT_ID = EventId.newId();
  private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();
  private static final AccountHolderId ACCOUNT_HOLDER_ID = AccountHolderId.newId();
  private static final AccountType ACCOUNT_TYPE = AccountType.CHECKING;
  private static final Currency CURRENCY = Currency.USD;
  private static final Clock FIXED_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final Instant OCCURRED_AT = Instant.now(FIXED_CLOCK);

  @Test
  void shouldCreateBankAccountOpenedEvent_whenAllArgumentsAreValid() {
    BankAccountOpenedEvent event =
      new BankAccountOpenedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        ACCOUNT_TYPE,
        CURRENCY,
        ACCOUNT_HOLDER_ID,
        OCCURRED_AT
      );

    assertThat(event.eventId()).isEqualTo(EVENT_ID);
    assertThat(event.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID);
    assertThat(event.accountType()).isEqualTo(ACCOUNT_TYPE);
    assertThat(event.currency()).isEqualTo(CURRENCY);
    assertThat(event.primaryAccountHolderId()).isEqualTo(ACCOUNT_HOLDER_ID);
    assertThat(event.occurredAt()).isEqualTo(OCCURRED_AT);
    assertThat(event.eventType())
      .isEqualTo(DomainEventType.BANK_ACCOUNT_OPENED);
    assertThat(event.eventType().value())
      .isEqualTo("bank-account-opened");
  }

  @Test
  void shouldThrowNullPointerException_whenEventIdIsNull() {
    assertThatThrownBy(() ->
      new BankAccountOpenedEvent(
        null,
        BANK_ACCOUNT_ID,
        ACCOUNT_TYPE,
        CURRENCY,
        ACCOUNT_HOLDER_ID,
        OCCURRED_AT
      )
    ).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldThrowNullPointerException_whenBankAccountIdIsNull() {
    assertThatThrownBy(() ->
      new BankAccountOpenedEvent(
        EVENT_ID,
        null,
        ACCOUNT_TYPE,
        CURRENCY,
        ACCOUNT_HOLDER_ID,
        OCCURRED_AT
      )
    ).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldThrowNullPointerException_whenAccountTypeIsNull() {
    assertThatThrownBy(() ->
      new BankAccountOpenedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        null,
        CURRENCY,
        ACCOUNT_HOLDER_ID,
        OCCURRED_AT
      )
    ).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldThrowNullPointerException_whenCurrencyIsNull() {
    assertThatThrownBy(() ->
      new BankAccountOpenedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        ACCOUNT_TYPE,
        null,
        ACCOUNT_HOLDER_ID,
        OCCURRED_AT
      )
    ).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldThrowNullPointerException_whenPrimaryAccountHolderIdIsNull() {
    assertThatThrownBy(() ->
      new BankAccountOpenedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        ACCOUNT_TYPE,
        CURRENCY,
        null,
        OCCURRED_AT
      )
    ).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldThrowNullPointerException_whenOccurredAtIsNull() {
    assertThatThrownBy(() ->
      new BankAccountOpenedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        ACCOUNT_TYPE,
        CURRENCY,
        ACCOUNT_HOLDER_ID,
        null
      )
    ).isInstanceOf(NullPointerException.class);
  }
}
