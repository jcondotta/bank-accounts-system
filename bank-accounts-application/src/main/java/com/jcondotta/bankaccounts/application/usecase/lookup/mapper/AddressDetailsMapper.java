package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.AddressDetails;
import com.jcondotta.bankaccounts.domain.value_objects.address.Address;
import org.springframework.stereotype.Component;

@Component
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