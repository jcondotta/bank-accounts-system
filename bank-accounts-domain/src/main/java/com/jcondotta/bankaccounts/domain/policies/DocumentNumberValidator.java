package com.jcondotta.bankaccounts.domain.policies;

import com.jcondotta.bankaccounts.domain.enums.DocumentCountry;
import com.jcondotta.bankaccounts.domain.enums.DocumentType;
import com.jcondotta.bankaccounts.domain.value_objects.personal.DocumentNumber;

public interface DocumentNumberValidator {

  DocumentCountry supportedCountry();
  DocumentType supportedType();

  void validate(DocumentNumber documentNumber);
}