package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookupbankaccount;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "AccountHolderDetailsResponse", description = "Represents the details of an account holder.")
public record AccountHolderDetailsResponse(

    @NotNull
    @Schema(description = "The UUID value representing the account holder identifier.",
        example = "c6a4a1b2-0f8c-41e3-a622-98d66de824a9",
        requiredMode = RequiredMode.REQUIRED)
    UUID id,

    @NotBlank
    @Schema(description = "Name of the account holder associated with this bank account.",
        example = "Jefferson Condotta",
        requiredMode = RequiredMode.REQUIRED)
    String name,

    @NotBlank
    @Schema(description = "Passport number of the account holder.",
      example = "FH254787",
      requiredMode = RequiredMode.REQUIRED)
    String passportNumber,

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Date of birth of the account holder.",
        example = "1988-02-01",
        requiredMode = RequiredMode.REQUIRED)
    LocalDate dateOfBirth,

    @NotNull
    @Schema(
      description = "Email address of the account holder.",
      example = "jefferson.condotta@email.com",
      requiredMode = RequiredMode.REQUIRED
    )
    String email,

    @NotNull
    @Schema(description = "Type of the account holder.",
        example = "PRIMARY", allowableValues = {"PRIMARY", "JOINT"},
        requiredMode = RequiredMode.REQUIRED)
    AccountHolderType accountHolderType,

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(
      description = "Date and time when the account holder was created (UTC).",
      example = "2023-08-23T12:55:00Z",
      requiredMode = RequiredMode.REQUIRED
    )
    Instant createdAt
) {
}
