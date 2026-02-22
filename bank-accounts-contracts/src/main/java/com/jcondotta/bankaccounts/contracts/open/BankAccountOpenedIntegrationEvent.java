package com.jcondotta.bankaccounts.contracts.open;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;

public record BankAccountOpenedIntegrationEvent(IntegrationEventMetadata metadata, BankAccountOpenedIntegrationPayload payload)
  implements IntegrationEvent<BankAccountOpenedIntegrationPayload> {

}
