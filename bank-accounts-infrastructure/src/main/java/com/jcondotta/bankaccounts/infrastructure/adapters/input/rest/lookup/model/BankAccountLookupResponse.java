package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookup.model;

import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookup.BankAccountDetailsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Response containing information for the looked-up bank account.")
public record BankAccountLookupResponse(

    @NotNull
    @Schema(description = "Details of the retrieved bank account.", requiredMode = RequiredMode.REQUIRED)
    BankAccountDetailsResponse bankAccount

) {}
