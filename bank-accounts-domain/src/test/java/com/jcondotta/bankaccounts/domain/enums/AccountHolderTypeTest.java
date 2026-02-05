package com.jcondotta.bankaccounts.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountHolderTypeTest {

  @Test
  void shouldIdentifyPrimaryAccountHolderType_whenTypeIsPrimary() {
    assertThat(AccountHolderType.PRIMARY.isPrimary()).isTrue();
    assertThat(AccountHolderType.JOINT.isPrimary()).isFalse();
  }

  @Test
  void shouldIdentifyJointAccountHolderType_whenTypeIsJoint() {
    assertThat(AccountHolderType.JOINT.isJoint()).isTrue();
    assertThat(AccountHolderType.PRIMARY.isJoint()).isFalse();
  }
}
