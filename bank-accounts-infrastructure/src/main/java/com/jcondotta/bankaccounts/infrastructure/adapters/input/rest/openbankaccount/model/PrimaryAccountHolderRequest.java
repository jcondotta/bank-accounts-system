package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model;

import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Request object used for creating a new account holder.")
public record PrimaryAccountHolderRequest(

  @Schema(description = "Name of the account holder", example = "Jefferson Condotta", requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  @Size(max = AccountHolderName.MAX_LENGTH,  message = "must not exceed {max} characters")
  String accountHolderName,

  @Schema(description = "Passport number of the account holder", example = "FH254787", requiredMode = RequiredMode.REQUIRED)
  @Size(min = PassportNumber.LENGTH, max = PassportNumber.LENGTH, message = "must have exactly {max} characters")
  @NotNull
  String passportNumber,

  @Schema(description = "Date of birth of the account holder", example = "1990-11-23", pattern = "yyyy-MM-dd", requiredMode = RequiredMode.REQUIRED)
  @Past
  @NotNull
  LocalDate dateOfBirth
) {
}