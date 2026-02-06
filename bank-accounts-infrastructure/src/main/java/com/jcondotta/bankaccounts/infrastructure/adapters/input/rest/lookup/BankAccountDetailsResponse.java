package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "BankAccountDetailsResponse", description = "Represents the details of a bank account.")
public record BankAccountDetailsResponse(

    @Schema(description = "The UUID value representing the bank account identifier.",
        example = "01920bff-1338-7efd-ade6-e9128debe5d4",
        requiredMode = RequiredMode.REQUIRED)
    UUID bankAccountId,

    @Schema(description = "Type of bank account (e.g., SAVINGS, CHECKING)", example = "SAVINGS",
        requiredMode = RequiredMode.REQUIRED, allowableValues = {"SAVINGS", "CHECKING"})
    AccountType accountType,

    @Schema(description = "Currency for the bank account (e.g., USD, EUR)", example = "USD", requiredMode = RequiredMode.REQUIRED)
    Currency currency,

    @Schema(description = "International Bank Account Number (IBAN) for the bank account.",
        example = "GB29NWBK60161331926819",
        maxLength = 34,
        requiredMode = RequiredMode.REQUIRED)
    String iban,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Schema(description = "Date and time when the bank account was opened.",
        example = "2023-08-23T14:55:00+02:00",
        requiredMode = RequiredMode.REQUIRED)
    ZonedDateTime dateOfOpening,

    @Schema(description = "Current status of the bank account", allowableValues = {"PENDING", "ACTIVE", "CANCELLED"})
    AccountStatus status,

    @Schema(description = "Account holders associated with this bank account.", requiredMode = RequiredMode.REQUIRED)
    List<AccountHolderDetailsResponse> accountHolders
) {
}
