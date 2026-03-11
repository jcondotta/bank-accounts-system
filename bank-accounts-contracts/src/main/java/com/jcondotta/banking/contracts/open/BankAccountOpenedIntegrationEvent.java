package com.jcondotta.banking.contracts.open;

import com.jcondotta.banking.contracts.IntegrationEvent;
import com.jcondotta.banking.contracts.IntegrationEventMetadata;

public record BankAccountOpenedIntegrationEvent(IntegrationEventMetadata metadata, BankAccountOpenedIntegrationPayload payload)
  implements IntegrationEvent<BankAccountOpenedIntegrationPayload> {

}
