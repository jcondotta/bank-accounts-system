package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.IdentityDocumentDetails;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.personal.IdentityDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
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