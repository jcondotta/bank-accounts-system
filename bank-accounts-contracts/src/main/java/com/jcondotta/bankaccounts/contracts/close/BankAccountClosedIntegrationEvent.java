package com.jcondotta.bankaccounts.contracts.close;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;

public record BankAccountClosedIntegrationEvent(IntegrationEventMetadata metadata, BankAccountClosedIntegrationPayload payload)
  implements IntegrationEvent<BankAccountClosedIntegrationPayload> {

}
