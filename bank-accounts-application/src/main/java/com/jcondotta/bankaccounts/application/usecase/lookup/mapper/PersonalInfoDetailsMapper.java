package com.jcondotta.bankaccounts.application.usecase.lookup.mapper;

import com.jcondotta.bankaccounts.application.usecase.lookup.model.PersonalInfoDetails;
import com.jcondotta.bankaccounts.domain.value_objects.personal.PersonalInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
  componentModel = "spring",
  uses = IdentityDocumentDetailsMapper.class,
  injectionStrategy = org.mapstruct.InjectionStrategy.CONSTRUCTOR
)
public interface PersonalInfoDetailsMapper {

  @Mapping(target = "firstName", expression = "java(personalInfo.holderName().firstName())")
  @Mapping(target = "lastName", expression = "java(personalInfo.holderName().lastName())")
  @Mapping(target = "identityDocument", source = "identityDocument")
  @Mapping(target = "dateOfBirth", expression = "java(personalInfo.dateOfBirth().value())")
  PersonalInfoDetails toDetails(PersonalInfo personalInfo);
}