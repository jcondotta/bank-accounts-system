package com.jcondotta.banking.recipients.domain.recipient.testsupport;

import com.jcondotta.banking.recipients.domain.recipient.aggregate.Recipient;
import com.jcondotta.banking.recipients.domain.recipient.enums.RecipientStatus;
import com.jcondotta.banking.recipients.domain.recipient.identity.RecipientId;
import com.jcondotta.banking.recipients.domain.recipient.value_objects.Iban;
import com.jcondotta.banking.recipients.domain.recipient.value_objects.RecipientName;

import java.time.Instant;

public final class RecipientBuilder {

  private RecipientId id;
  private RecipientName name;
  private Iban iban;
  private RecipientStatus status;
  private Instant createdAt;

  private RecipientBuilder() {}

  public static RecipientBuilder from(RecipientFixtures fixture) {
    var builder = new RecipientBuilder();

    builder.id = RecipientId.newId();
    builder.name = fixture.toName();
    builder.iban = fixture.toIban();
    builder.status = RecipientStatus.ACTIVE;
    builder.createdAt = Instant.now(ClockTestFactory.FIXED_CLOCK);

    return builder;
  }

  public RecipientBuilder withId(RecipientId id) {
    this.id = id;
    return this;
  }

  public RecipientBuilder withName(String name) {
    this.name = RecipientName.of(name);
    return this;
  }

  public RecipientBuilder withIban(String iban) {
    this.iban = Iban.of(iban);
    return this;
  }

  public RecipientBuilder removed() {
    this.status = RecipientStatus.REMOVED;
    return this;
  }

  public RecipientBuilder active() {
    this.status = RecipientStatus.ACTIVE;
    return this;
  }

  public RecipientBuilder createdAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public Recipient build() {
    return Recipient.restore(
      id,
      name,
      iban,
      status,
      createdAt
    );
  }
}