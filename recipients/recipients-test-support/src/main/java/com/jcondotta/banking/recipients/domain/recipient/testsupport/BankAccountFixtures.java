package com.jcondotta.banking.recipients.domain.recipient.testsupport;


import com.jcondotta.banking.recipients.domain.recipient.aggregate.BankAccount;

import java.util.List;

public enum BankAccountFixtures {

  WITH_NO_RECIPIENTS(),
  ACCOUNT_WITH_JEFFERSON(RecipientFixtures.JEFFERSON),
  ACCOUNT_WITH_PATRIZIO(RecipientFixtures.PATRIZIO),
  ACCOUNT_WITH_TWO_RECIPIENTS(RecipientFixtures.JEFFERSON, RecipientFixtures.PATRIZIO);

  private final List<RecipientFixtures> recipients;

  BankAccountFixtures(RecipientFixtures... fixtures) {
    this.recipients = List.of(fixtures);
  }

  public List<RecipientFixtures> recipients() {
    return recipients;
  }

  public BankAccount create() {
    return BankAccountBuilder.from(this).build();
  }
}