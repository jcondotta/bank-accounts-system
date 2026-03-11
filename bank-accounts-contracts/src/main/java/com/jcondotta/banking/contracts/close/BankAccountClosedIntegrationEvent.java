package com.jcondotta.banking.contracts.close;

import com.jcondotta.banking.contracts.IntegrationEvent;
import com.jcondotta.banking.contracts.IntegrationEventMetadata;

public record BankAccountClosedIntegrationEvent(IntegrationEventMetadata metadata, BankAccountClosedIntegrationPayload payload)
  implements IntegrationEvent<BankAccountClosedIntegrationPayload> {

}
