package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.open.BankAccountOpenedIntegrationEvent;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.BankAccountClosedEvent;
import com.jcondotta.bankaccounts.domain.events.BankAccountOpenedEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.EventId;
import com.jcondotta.bankaccounts.infrastructure.config.ClockTestFactory;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountOpenedIntegrationEventMapperTest {

  private static final EventId EVENT_ID = EventId.newId();
  private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();
  private static final AccountHolderId ACCOUNT_HOLDER_ID = AccountHolderId.newId();

  private final Instant NOW = Instant.now(ClockTestFactory.FIXED_CLOCK);

  private static final UUID CORRELATION_ID = UUID.randomUUID();

  private final BankAccountOpenedIntegrationEventMapper mapper = new BankAccountOpenedIntegrationEventMapper();

  @Test
  void shouldMapToBankAccountOpenedIntegrationEvent_whenDomainEventIsBankAccountOpenedEvent() {
    var bankAccountOpenedEvent = new BankAccountOpenedEvent(
      EVENT_ID,
      BANK_ACCOUNT_ID,
      AccountType.CHECKING,
      Currency.EUR,
      ACCOUNT_HOLDER_ID,
      NOW
    );

    IntegrationEvent<?> integrationEvent = mapper.toIntegrationEvent(bankAccountOpenedEvent, CORRELATION_ID);

    assertThat(integrationEvent)
      .isInstanceOfSatisfying(BankAccountOpenedIntegrationEvent.class, event -> {
        var metadata = event.metadata();

        assertThat(metadata.eventId()).isEqualTo(EVENT_ID.value());
        assertThat(metadata.correlationId()).isEqualTo(CORRELATION_ID);
        assertThat(metadata.eventType()).isEqualTo(bankAccountOpenedEvent.eventType().value());
        assertThat(metadata.version()).isEqualTo(AbstractDomainEventMapper.METADATA_VERSION);
        assertThat(metadata.occurredAt()).isEqualTo(NOW);

        var payload = event.payload();
        assertThat(payload.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID.value());
        assertThat(payload.accountType()).isEqualTo(AccountType.CHECKING.name());
        assertThat(payload.currency()).isEqualTo(Currency.EUR.name());
        assertThat(payload.accountHolderId()).isEqualTo(ACCOUNT_HOLDER_ID.value());
      });
  }

  @Test
  void shouldReturnMappedEventType() {
    assertThat(mapper.mappedEventType()).isEqualTo(BankAccountOpenedEvent.class);
  }

  @Test
  void shouldThrowClassCastException_whenDomainEventTypeDoesNotMatchMapper() {
    DomainEvent wrongEvent = new BankAccountClosedEvent(
      EVENT_ID,
      BANK_ACCOUNT_ID,
      NOW
    );

    assertThatThrownBy(() -> mapper.toIntegrationEvent(wrongEvent, CORRELATION_ID))
      .isInstanceOf(ClassCastException.class);
  }
}