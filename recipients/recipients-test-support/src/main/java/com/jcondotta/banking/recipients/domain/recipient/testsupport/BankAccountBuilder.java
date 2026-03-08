package com.jcondotta.banking.recipients.domain.recipient.testsupport;

import com.jcondotta.banking.recipients.domain.recipient.aggregate.BankAccount;
import com.jcondotta.banking.recipients.domain.recipient.aggregate.Recipient;
import com.jcondotta.banking.recipients.domain.recipient.aggregate.Recipients;
import com.jcondotta.banking.recipients.domain.recipient.enums.AccountStatus;
import com.jcondotta.banking.recipients.domain.recipient.identity.BankAccountId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BankAccountBuilder {

  private BankAccountId id;
  private AccountStatus status;
  private final List<Recipient> recipients = new ArrayList<>();

  private BankAccountBuilder() {}

  public static BankAccountBuilder from(BankAccountFixtures accountFixtures) {
    var builder = new BankAccountBuilder();

    builder.id = BankAccountId.of(UUID.randomUUID());
    builder.status = AccountStatus.ACTIVE;

    accountFixtures.recipients()
      .stream()
      .map(fixture -> RecipientBuilder.from(fixture).build())
      .forEach(builder.recipients::add);

    return builder;
  }

  public BankAccountBuilder withId(BankAccountId id) {
    this.id = id;
    return this;
  }

  public BankAccountBuilder closed() {
    this.status = AccountStatus.CLOSED;
    return this;
  }

  public BankAccountBuilder active() {
    this.status = AccountStatus.ACTIVE;
    return this;
  }

  public BankAccountBuilder addRecipient(Recipient recipient) {
    this.recipients.add(recipient);
    return this;
  }

  public BankAccount build() {
    return BankAccount.restore(
      id,
      status,
      Recipients.of(recipients)
    );
  }
}