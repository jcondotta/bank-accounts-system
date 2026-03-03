package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.IdentityDocumentDetails;
import com.jcondotta.bankaccounts.domain.value_objects.personal.IdentityDocument;
import org.springframework.stereotype.Component;

@Component
public interface IdentityDocumentDetailsMapper {

  default IdentityDocumentDetails toDetails(IdentityDocument identityDocument) {

    if (identityDocument == null) {
      return null;
    }

    return new IdentityDocumentDetails(
      identityDocument.country().name(),
      identityDocument.type().name(),
      identityDocument.number().value()
    );
  }
}