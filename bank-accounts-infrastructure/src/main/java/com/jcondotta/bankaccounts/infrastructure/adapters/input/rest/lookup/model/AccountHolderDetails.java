package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.lookup.model;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;

import java.time.ZonedDateTime;

public record AccountHolderDetails(
    AccountHolderId accountHolderId,
    AccountHolderName accountHolderName,
    PassportNumber passportNumber,
    DateOfBirth dateOfBirth,
    AccountHolderType accountHolderType,
    ZonedDateTime createdAt
) { }
