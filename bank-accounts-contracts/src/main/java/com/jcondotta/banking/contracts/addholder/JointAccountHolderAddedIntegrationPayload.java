package com.jcondotta.banking.contracts.addholder;

import java.util.UUID;

public record JointAccountHolderAddedIntegrationPayload(UUID bankAccountId, UUID accountHolderId) {
}
