package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.address.Address;
import com.jcondotta.bankaccounts.domain.value_objects.contact.ContactInfo;
import com.jcondotta.bankaccounts.domain.value_objects.personal.PersonalInfo;

import java.time.Instant;

import static com.jcondotta.bankaccounts.domain.validation.DomainPreconditions.required;

public final class AccountHolder {

  static final String ID_MUST_BE_PROVIDED = "Account holder id must be provided";
  static final String PERSONAL_INFO_MUST_BE_PROVIDED = "Account holder personal info must be provided";
  static final String CONTACT_INFO_MUST_BE_PROVIDED = "Account holder contact info must be provided";
  static final String ADDRESS_MUST_BE_PROVIDED = "Account holder address must be provided";
  static final String ACCOUNT_HOLDER_TYPE_MUST_BE_PROVIDED = "Account holder type must be provided";
  static final String CREATED_AT_MUST_BE_PROVIDED = "Created at must be provided";

  private final AccountHolderId id;
  private final PersonalInfo personalInfo;
  private final ContactInfo contactInfo;
  private final Address address;
  private final AccountHolderType accountHolderType;
  private final Instant createdAt;

  private AccountHolder(
    AccountHolderId id,
    PersonalInfo personalInfo,
    ContactInfo contactInfo,
    Address address,
    AccountHolderType accountHolderType,
    Instant createdAt
  ) {
    this.id = required(id, ID_MUST_BE_PROVIDED);
    this.personalInfo = required(personalInfo, PERSONAL_INFO_MUST_BE_PROVIDED);
    this.contactInfo = required(contactInfo, CONTACT_INFO_MUST_BE_PROVIDED);
    this.address = required(address, ADDRESS_MUST_BE_PROVIDED);
    this.accountHolderType = required(accountHolderType, ACCOUNT_HOLDER_TYPE_MUST_BE_PROVIDED);
    this.createdAt = required(createdAt, CREATED_AT_MUST_BE_PROVIDED);
  }

  static AccountHolder createPrimary(PersonalInfo personalInfo, ContactInfo contactInfo, Address address, Instant createdAt) {
    return create(personalInfo, contactInfo, address, AccountHolderType.PRIMARY, createdAt);
  }

  static AccountHolder createJoint(PersonalInfo personalInfo, ContactInfo contactInfo, Address address, Instant createdAt) {
    return create(personalInfo, contactInfo, address, AccountHolderType.JOINT, createdAt);
  }

  static AccountHolder create(PersonalInfo personalInfo, ContactInfo contactInfo, Address address, AccountHolderType accountHolderType, Instant createdAt) {
    return new AccountHolder(AccountHolderId.newId(), personalInfo, contactInfo, address, accountHolderType, createdAt);
  }

  static AccountHolder restore(
    AccountHolderId accountHolderId,
    PersonalInfo personalInfo,
    ContactInfo contactInfo,
    Address address,
    AccountHolderType accountHolderType,
    Instant createdAt) {

    return new AccountHolder(accountHolderId, personalInfo, contactInfo, address, accountHolderType, createdAt);
  }

  public AccountHolderId id() {
    return id;
  }

  public PersonalInfo personalInfo() {
    return personalInfo;
  }

  public ContactInfo contactInfo() {
    return contactInfo;
  }

  public Address address() {
    return address;
  }

  public AccountHolderType accountHolderType() {
    return accountHolderType;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public boolean isPrimary() {
    return accountHolderType.isPrimary();
  }

  public boolean isJoint() {
    return accountHolderType.isJoint();
  }
}
