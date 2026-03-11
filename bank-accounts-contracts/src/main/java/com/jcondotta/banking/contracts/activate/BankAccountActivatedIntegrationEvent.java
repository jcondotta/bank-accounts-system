package com.jcondotta.banking.contracts.activate;

import com.jcondotta.banking.contracts.DefaultIntegrationEventMetadata;
import com.jcondotta.banking.contracts.IntegrationEvent;

public record BankAccountActivatedIntegrationEvent(DefaultIntegrationEventMetadata metadata, BankAccountActivatedIntegrationPayload payload)
  implements IntegrationEvent<BankAccountActivatedIntegrationPayload> {

}
