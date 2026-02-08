package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;
import com.jcondotta.bankaccounts.domain.validation.DomainEventValidationErrors;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EventIdTest {

  private static final UUID EVENT_UUID_1 = UUID.fromString("8b9f0f3a-7f3d-4b7f-9a21-3b1c9c1f8a11");
  private static final UUID EVENT_UUID_2 = UUID.fromString("4e0d6c7b-5b1a-4b62-9a4d-2e6a4c8f3e55");

  @Test
  void shouldCreateEventId_whenValueIsValid() {
    var eventId = EventId.of(EVENT_UUID_1);

    assertThat(eventId)
      .extracting(EventId::value)
      .isEqualTo(EVENT_UUID_1);
  }

  @Test
  void shouldGenerateNewEventId() {
    var eventId = EventId.newId();

    assertThat(eventId)
      .extracting(EventId::value)
      .isNotNull();
  }

  @Test
  void shouldThrowDomainValidationException_whenValueIsNull() {
    assertThatThrownBy(() -> EventId.of(null))
      .isInstanceOf(DomainValidationException.class)
      .hasMessage(DomainEventValidationErrors.EVENT_ID_NOT_NULL);
  }

  @Test
  void shouldBeEqual_whenEventIdsHaveSameValue() {
    var eventId1 = EventId.of(EVENT_UUID_1);
    var eventId2 = EventId.of(EVENT_UUID_1);

    assertThat(eventId1)
      .isEqualTo(eventId2)
      .hasSameHashCodeAs(eventId2);
  }

  @Test
  void shouldNotBeEqual_whenEventIdsHaveDifferentValues() {
    var eventId1 = EventId.of(EVENT_UUID_1);
    var eventId2 = EventId.of(EVENT_UUID_2);

    assertThat(eventId1).isNotEqualTo(eventId2);
  }
}