package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.ContactInfoDetails;
import com.jcondotta.bankaccounts.domain.value_objects.contact.ContactInfo;
import com.jcondotta.bankaccounts.domain.value_objects.contact.Email;
import com.jcondotta.bankaccounts.domain.value_objects.contact.PhoneNumber;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContactInfoDetailsMapperTest {

  private static final Email EMAIL = Email.of("jefferson.condotta@email.com");
  private static final PhoneNumber PHONE = PhoneNumber.of("+34600111222");

  private final ContactInfoDetailsMapper mapper = new ContactInfoDetailsMapper() {
  };

  @Test
  void shouldMapContactInfoDetails_whenEmailAndPhoneArePresent() {
    ContactInfo contactInfo = ContactInfo.of(EMAIL, PHONE);

    ContactInfoDetails details = mapper.toDetails(contactInfo);

    assertThat(details.email()).isEqualTo(EMAIL.value());
    assertThat(details.phoneNumber()).isEqualTo(PHONE.value());
  }

  @Test
  void shouldReturnNull_whenContactInfoIsNull() {
    ContactInfoDetails details = mapper.toDetails(null);

    assertThat(details).isNull();
  }
}