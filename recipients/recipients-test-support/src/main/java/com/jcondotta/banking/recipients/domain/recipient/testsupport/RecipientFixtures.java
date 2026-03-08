package com.jcondotta.banking.recipients.domain.recipient.testsupport;

import com.jcondotta.banking.recipients.domain.recipient.aggregate.Recipient;
import com.jcondotta.banking.recipients.domain.recipient.value_objects.Iban;
import com.jcondotta.banking.recipients.domain.recipient.value_objects.RecipientName;

public enum RecipientFixtures {

  JEFFERSON("Jefferson Condotta", "ES3801283316232166447417"),
  PATRIZIO("Patrizio Condotta", "IT93Q0300203280175171887193"),
  VIRGINIO("Virginio Condotta", "GB82WEST12345698765432");

  private final String name;
  private final String iban;

  RecipientFixtures(String name, String iban) {
    this.name = name;
    this.iban = iban;
  }

  public RecipientName toName() {
    return RecipientName.of(name);
  }

  public Iban toIban() {
    return Iban.of(iban);
  }

  public Recipient toRecipient() {
    return RecipientBuilder.from(this).build();
  }
}