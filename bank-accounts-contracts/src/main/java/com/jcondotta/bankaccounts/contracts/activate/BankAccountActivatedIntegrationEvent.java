package com.jcondotta.bankaccounts.contracts.activate;

import com.jcondotta.bankaccounts.contracts.DefaultIntegrationEventMetadata;
import com.jcondotta.bankaccounts.contracts.IntegrationEvent;

public record BankAccountActivatedIntegrationEvent(DefaultIntegrationEventMetadata metadata, BankAccountActivatedIntegrationPayload payload)
  implements IntegrationEvent<BankAccountActivatedIntegrationPayload> {

}
