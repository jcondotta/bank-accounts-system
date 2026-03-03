package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.AddressDetails;
import com.jcondotta.bankaccounts.domain.value_objects.address.Address;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddressDetailsMapperTest {

  private static final String STREET = "Carrer de Mallorca";
  private static final String NUMBER = "456";
  private static final String COMPLEMENT = "2º Andar";
  private static final String POSTAL_CODE = "08013";
  private static final String CITY = "Barcelona";

  private final AddressDetailsMapper mapper = new AddressDetailsMapper() {
  };

  @Test
  void shouldMapAddressDetails_whenComplementIsPresent() {
    Address address = Address.of(STREET, NUMBER, COMPLEMENT, POSTAL_CODE, CITY);

    AddressDetails details = mapper.toDetails(address);

    assertThat(details.street()).isEqualTo(STREET);
    assertThat(details.streetNumber()).isEqualTo(NUMBER);
    assertThat(details.addressComplement()).isEqualTo(COMPLEMENT);
    assertThat(details.postalCode()).isEqualTo(POSTAL_CODE);
    assertThat(details.city()).isEqualTo(CITY);
  }

  @Test
  void shouldMapAddressDetails_whenComplementIsNull() {
    Address address = Address.of(STREET, NUMBER, null, POSTAL_CODE, CITY);

    AddressDetails details = mapper.toDetails(address);

    assertThat(details.street()).isEqualTo(STREET);
    assertThat(details.streetNumber()).isEqualTo(NUMBER);
    assertThat(details.addressComplement()).isNull();
    assertThat(details.postalCode()).isEqualTo(POSTAL_CODE);
    assertThat(details.city()).isEqualTo(CITY);
  }

  @Test
  void shouldReturnNull_whenAddressIsNull() {
    AddressDetails details = mapper.toDetails(null);

    assertThat(details).isNull();
  }
}