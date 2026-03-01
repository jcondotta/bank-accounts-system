package com.jcondotta.bankaccounts.domain.fixtures;

import com.jcondotta.bankaccounts.domain.enums.DocumentCountry;
import com.jcondotta.bankaccounts.domain.enums.DocumentType;
import com.jcondotta.bankaccounts.domain.value_objects.personal.DocumentNumber;
import com.jcondotta.bankaccounts.domain.value_objects.personal.IdentityDocument;

public enum IdentityDocumentFixtures {

  SPANISH_DNI(
    DocumentCountry.SPAIN,
    DocumentType.NATIONAL_ID,
    "12345678Z"
  ),

  SPANISH_NIE(
    DocumentCountry.SPAIN,
    DocumentType.FOREIGNER_ID,
    "X7566995H"
  ),

  SPANISH_PASSPORT(
    DocumentCountry.SPAIN,
    DocumentType.PASSPORT,
    "QWE000123"
  );

  private final DocumentCountry country;
  private final DocumentType type;
  private final String documentNumber;

  IdentityDocumentFixtures(
    DocumentCountry country,
    DocumentType type,
    String documentNumber
  ) {
    this.country = country;
    this.type = type;
    this.documentNumber = documentNumber;
  }

  public IdentityDocument identityDocument() {
    return IdentityDocument.of(
      country,
      type,
      DocumentNumber.of(documentNumber)
    );
  }

  public DocumentCountry country() {
    return country;
  }

  public DocumentType documentType() {
    return type;
  }

  public DocumentNumber documentNumber() {
    return DocumentNumber.of(documentNumber);
  }
}