package com.jcondotta.bankaccounts.contracts.activate;

import java.util.UUID;

public record BankAccountActivatedIntegrationPayload(UUID bankAccountId) {
}