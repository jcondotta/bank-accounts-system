package com.jcondotta.banking.contracts.addholder;

import com.jcondotta.banking.contracts.IntegrationEvent;
import com.jcondotta.banking.contracts.IntegrationEventMetadata;

public record JointAccountHolderAddedIntegrationEvent(IntegrationEventMetadata metadata, JointAccountHolderAddedIntegrationPayload payload)
  implements IntegrationEvent<JointAccountHolderAddedIntegrationPayload> {

}
