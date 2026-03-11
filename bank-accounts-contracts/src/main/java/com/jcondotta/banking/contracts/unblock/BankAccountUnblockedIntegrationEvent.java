package com.jcondotta.banking.contracts.unblock;

import com.jcondotta.banking.contracts.IntegrationEvent;
import com.jcondotta.banking.contracts.IntegrationEventMetadata;

public record BankAccountUnblockedIntegrationEvent(IntegrationEventMetadata metadata, BankAccountUnblockedIntegrationPayload payload)
  implements IntegrationEvent<BankAccountUnblockedIntegrationPayload> {

}
