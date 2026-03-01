package com.jcondotta.bankaccounts.domain.policies;

import com.jcondotta.bankaccounts.domain.enums.DocumentCountry;
import com.jcondotta.bankaccounts.domain.enums.DocumentType;

public interface DocumentNumberValidatorRegistry {

  DocumentNumberValidator resolve(DocumentCountry country, DocumentType type);
}