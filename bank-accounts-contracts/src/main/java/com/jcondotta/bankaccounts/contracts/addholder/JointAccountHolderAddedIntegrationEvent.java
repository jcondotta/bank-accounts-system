package com.jcondotta.bankaccounts.contracts.addholder;

import com.jcondotta.bankaccounts.contracts.IntegrationEvent;
import com.jcondotta.bankaccounts.contracts.IntegrationEventMetadata;

public record JointAccountHolderAddedIntegrationEvent(IntegrationEventMetadata metadata, JointAccountHolderAddedIntegrationPayload payload)
  implements IntegrationEvent<JointAccountHolderAddedIntegrationPayload> {

}
