package com.jcondotta.bankaccounts.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountStatusTest {

  @Test
  void shouldIdentifyPendingStatus_whenStatusIsPending() {
    assertThat(AccountStatus.PENDING.isPending()).isTrue();
    assertThat(AccountStatus.ACTIVE.isPending()).isFalse();
    assertThat(AccountStatus.CANCELLED.isPending()).isFalse();
    assertThat(AccountStatus.BLOCKED.isPending()).isFalse();
  }

  @Test
  void shouldIdentifyActiveStatus_whenStatusIsActive() {
    assertThat(AccountStatus.ACTIVE.isActive()).isTrue();
    assertThat(AccountStatus.PENDING.isActive()).isFalse();
    assertThat(AccountStatus.CANCELLED.isActive()).isFalse();
    assertThat(AccountStatus.BLOCKED.isActive()).isFalse();
  }

  @Test
  void shouldIdentifyCancelledStatus_whenStatusIsCancelled() {
    assertThat(AccountStatus.CANCELLED.isCancelled()).isTrue();
    assertThat(AccountStatus.PENDING.isCancelled()).isFalse();
    assertThat(AccountStatus.ACTIVE.isCancelled()).isFalse();
    assertThat(AccountStatus.BLOCKED.isCancelled()).isFalse();
  }

  @Test
  void shouldIdentifyBlockedStatus_whenStatusIsBlocked() {
    assertThat(AccountStatus.BLOCKED.isBlocked()).isTrue();
    assertThat(AccountStatus.PENDING.isBlocked()).isFalse();
    assertThat(AccountStatus.ACTIVE.isBlocked()).isFalse();
    assertThat(AccountStatus.CANCELLED.isBlocked()).isFalse();
  }
}
