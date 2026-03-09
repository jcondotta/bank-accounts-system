package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.AddressDetails;
import com.jcondotta.banking.accounts.domain.bankaccount.value_objects.address.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressDetailsMapper {

  default AddressDetails toDetails(Address address) {
    if (address == null) {
      return null;
    }

    return new AddressDetails(
      address.street().value(),
      address.streetNumber().value(),
      address.complement() != null ? address.complement().value() : null,
      address.postalCode().value(),
      address.city().value()
    );
  }
}