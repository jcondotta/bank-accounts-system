package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.events.JointAccountHolderAddedEvent;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotActiveException;
import com.jcondotta.bankaccounts.domain.exceptions.MaxJointAccountHoldersExceededException;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.fixtures.BankAccountTestFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountAddJointAccountHolderTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;
  private static final AccountHolderFixtures JOINT_ACCOUNT_HOLDER_1 = AccountHolderFixtures.PATRIZIO;
  private static final AccountHolderFixtures JOINT_ACCOUNT_HOLDER_2 = AccountHolderFixtures.VIRGINIO;

  @Test
  void shouldAddJointAccountHolder_whenAccountIsActive() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.addJointAccountHolder(
      JOINT_ACCOUNT_HOLDER_1.personalInfo(),
      JOINT_ACCOUNT_HOLDER_1.contactInfo(),
      JOINT_ACCOUNT_HOLDER_1.address()
    );

    assertThat(bankAccount.accountHolders())
      .hasSize(2)
      .filteredOn(AccountHolder::isJoint)
      .hasSize(1);

    var jointHolder = bankAccount.jointAccountHolders().getFirst();

    assertThat(jointHolder.personalInfo()).isEqualTo(JOINT_ACCOUNT_HOLDER_1.personalInfo());
    assertThat(jointHolder.contactInfo()).isEqualTo(JOINT_ACCOUNT_HOLDER_1.contactInfo());
    assertThat(jointHolder.address()).isEqualTo(JOINT_ACCOUNT_HOLDER_1.address());
    assertThat(jointHolder.createdAt()).isNotNull();

    var events = bankAccount.pullEvents();

    assertThat(events)
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(JointAccountHolderAddedEvent.class, event -> {
        assertThat(event.bankAccountId()).isEqualTo(bankAccount.id());
        assertThat(event.accountHolderId()).isEqualTo(jointHolder.id());
        assertThat(event.occurredAt()).isNotNull();
      });

    assertThat(bankAccount.pullEvents()).isEmpty();
  }

  @Test
  void shouldThrowBankAccountNotActiveException_whenAccountIsNotActive() {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);

    assertThatThrownBy(() ->
      bankAccount.addJointAccountHolder(
        JOINT_ACCOUNT_HOLDER_1.personalInfo(),
        JOINT_ACCOUNT_HOLDER_1.contactInfo(),
        JOINT_ACCOUNT_HOLDER_1.address()
      ))
      .isInstanceOf(BankAccountNotActiveException.class);
  }

  @Test
  void shouldThrowMaxJointAccountHoldersExceededException_whenJointLimitIsReached() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.addJointAccountHolder(
      JOINT_ACCOUNT_HOLDER_1.personalInfo(),
      JOINT_ACCOUNT_HOLDER_1.contactInfo(),
      JOINT_ACCOUNT_HOLDER_1.address()
    );

    assertThatThrownBy(() ->
      bankAccount.addJointAccountHolder(
        JOINT_ACCOUNT_HOLDER_2.personalInfo(),
        JOINT_ACCOUNT_HOLDER_2.contactInfo(),
        JOINT_ACCOUNT_HOLDER_2.address()
      ))
      .isInstanceOf(MaxJointAccountHoldersExceededException.class);
  }
}
