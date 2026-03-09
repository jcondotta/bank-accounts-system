package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.model;

import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.common.AddressRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.common.ContactInfoRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.common.PersonalInfoRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request object used for creating a new account holder.")
public record AddJointAccountHolderRequest(

  @Valid
  @NotNull
  @Schema(
    description = "Personal information of the account holder.",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  PersonalInfoRequest personalInfo,

  @Valid
  @NotNull
  @Schema(
    description = "Contact information of the account holder.",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  ContactInfoRequest contactInfo,

  @Valid
  @NotNull
  @Schema(
    description = "Address of the account holder.",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  AddressRequest address
) {
}