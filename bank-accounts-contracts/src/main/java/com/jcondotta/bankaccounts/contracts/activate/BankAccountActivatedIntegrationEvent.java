package com.jcondotta.bankaccounts.contracts.activate;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;

public record BankAccountActivatedIntegrationEvent(IntegrationEventMetadata metadata, BankAccountActivatedIntegrationPayload payload)
  implements IntegrationEvent<BankAccountActivatedIntegrationPayload> {

}
