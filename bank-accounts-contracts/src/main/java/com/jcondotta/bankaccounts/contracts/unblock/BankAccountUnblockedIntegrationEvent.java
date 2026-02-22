package com.jcondotta.bankaccounts.contracts.unblock;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;

public record BankAccountUnblockedIntegrationEvent(IntegrationEventMetadata metadata, BankAccountUnblockedIntegrationPayload payload)
  implements IntegrationEvent<BankAccountUnblockedIntegrationPayload> {

}
