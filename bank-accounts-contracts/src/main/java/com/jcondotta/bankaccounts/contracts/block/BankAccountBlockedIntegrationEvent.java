package com.jcondotta.bankaccounts.contracts.block;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;

public record BankAccountBlockedIntegrationEvent(IntegrationEventMetadata metadata, BankAccountBlockedIntegrationPayload payload)
  implements IntegrationEvent<BankAccountBlockedIntegrationPayload> {

}
