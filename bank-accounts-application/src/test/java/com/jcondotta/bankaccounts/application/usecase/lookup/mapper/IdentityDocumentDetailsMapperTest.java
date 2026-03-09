package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.IdentityDocumentDetails;
import com.jcondotta.banking.accounts.domain.bankaccount.enums.DocumentCountry;
import com.jcondotta.banking.accounts.domain.bankaccount.enums.DocumentType;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.personal.DocumentNumber;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.personal.IdentityDocument;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentityDocumentDetailsMapperTest {

  private static final DocumentCountry COUNTRY = DocumentCountry.SPAIN;
  private static final DocumentType TYPE = DocumentType.NATIONAL_ID;
  private static final String NUMBER = "12345678Z";

  private final IdentityDocumentDetailsMapper mapper = new IdentityDocumentDetailsMapper() {
  };

  @Test
  void shouldMapIdentityDocumentDetails_whenIdentityDocumentIsValid() {

    IdentityDocument identityDocument =
      IdentityDocument.of(COUNTRY, TYPE, DocumentNumber.of(NUMBER));

    IdentityDocumentDetails details = mapper.toDetails(identityDocument);

    assertThat(details.country()).isEqualTo(COUNTRY.name());
    assertThat(details.type()).isEqualTo(TYPE.name());
    assertThat(details.number()).isEqualTo(NUMBER);
  }

  @Test
  void shouldReturnNull_whenIdentityDocumentIsNull() {

    IdentityDocumentDetails details = mapper.toDetails(null);

    assertThat(details).isNull();
  }
}