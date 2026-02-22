package com.jcondotta.bankaccounts.infrastructure.adapters.output.messaging.mapper;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.addholder.JointAccountHolderAddedIntegrationEvent;
import com.jcondotta.bankaccounts.domain.events.JointAccountHolderAddedEvent;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.EventId;
import com.jcondotta.bankaccounts.infrastructure.config.ClockTestFactory;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JointAccountHolderAddedIntegrationEventMapperTest {

  private static final EventId EVENT_ID = EventId.newId();
  private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();
  private static final AccountHolderId ACCOUNT_HOLDER_ID = AccountHolderId.newId();
  private static final UUID CORRELATION_ID = UUID.randomUUID();
  private static final Instant NOW =
    Instant.now(ClockTestFactory.FIXED_CLOCK);

  private final JointAccountHolderAddedIntegrationEventMapper mapper =
    new JointAccountHolderAddedIntegrationEventMapper();

  @Test
  void shouldMapToAddJointAccountHolderIntegrationEvent_whenDomainEventIsAddJointAccountHolderEvent() {

    JointAccountHolderAddedEvent event =
      new JointAccountHolderAddedEvent(
        EVENT_ID,
        BANK_ACCOUNT_ID,
        ACCOUNT_HOLDER_ID,
        NOW
      );

    IntegrationEvent<?> integrationEvent =
      mapper.toIntegrationEvent(event, CORRELATION_ID);

    assertThat(integrationEvent)
      .isInstanceOfSatisfying(JointAccountHolderAddedIntegrationEvent.class, mapped -> {

        var metadata = mapped.metadata();

        assertThat(metadata.eventId()).isEqualTo(EVENT_ID.value());
        assertThat(metadata.correlationId()).isEqualTo(CORRELATION_ID);
        assertThat(metadata.eventType()).isEqualTo(event.eventType().value());
        assertThat(metadata.version()).isEqualTo(AbstractDomainEventMapper.METADATA_VERSION);
        assertThat(metadata.occurredAt()).isEqualTo(NOW);

        var payload = mapped.payload();

        assertThat(payload.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID.value());
        assertThat(payload.accountHolderId()).isEqualTo(ACCOUNT_HOLDER_ID.value());
      });
  }

  @Test
  void shouldReturnMappedEventType() {
    assertThat(mapper.mappedEventType()).isEqualTo(JointAccountHolderAddedEvent.class);
  }
}