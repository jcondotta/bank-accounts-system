package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.model;

import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount.AccountHolderDetailsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Response containing information for the created joint account holder.")
public record AddJointAccountHolderResponse(

    @NotNull
    @Schema(description = "Details of the created joint account holder", requiredMode = Schema.RequiredMode.REQUIRED)
    AccountHolderDetailsResponse accountHolder

){ }