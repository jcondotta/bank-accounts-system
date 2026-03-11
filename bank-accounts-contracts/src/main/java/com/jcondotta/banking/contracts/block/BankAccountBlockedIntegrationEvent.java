package com.jcondotta.banking.contracts.block;

import com.jcondotta.banking.contracts.IntegrationEvent;
import com.jcondotta.banking.contracts.IntegrationEventMetadata;

public record BankAccountBlockedIntegrationEvent(IntegrationEventMetadata metadata, BankAccountBlockedIntegrationPayload payload)
  implements IntegrationEvent<BankAccountBlockedIntegrationPayload> {

}
