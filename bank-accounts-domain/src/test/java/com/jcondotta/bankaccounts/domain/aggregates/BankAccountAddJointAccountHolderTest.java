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
      JOINT_ACCOUNT_HOLDER_1.getAccountHolderName(),
      JOINT_ACCOUNT_HOLDER_1.getPassportNumber(),
      JOINT_ACCOUNT_HOLDER_1.getDateOfBirth(),
      JOINT_ACCOUNT_HOLDER_1.getEmail()
    );

    assertThat(bankAccount.accountHolders())
      .hasSize(2)
      .filteredOn(AccountHolder::isJoint)
      .hasSize(1);

    var jointHolder = bankAccount.jointAccountHolders().getFirst();

    assertThat(jointHolder.name()).isEqualTo(JOINT_ACCOUNT_HOLDER_1.getAccountHolderName());
    assertThat(jointHolder.passportNumber()).isEqualTo(JOINT_ACCOUNT_HOLDER_1.getPassportNumber());
    assertThat(jointHolder.dateOfBirth()).isEqualTo(JOINT_ACCOUNT_HOLDER_1.getDateOfBirth());
    assertThat(jointHolder.email()).isEqualTo(JOINT_ACCOUNT_HOLDER_1.getEmail());
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
  }

  @Test
  void shouldThrowBankAccountNotActiveException_whenAccountIsNotActive() {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);

    assertThatThrownBy(() ->
      bankAccount.addJointAccountHolder(
        JOINT_ACCOUNT_HOLDER_1.getAccountHolderName(),
        JOINT_ACCOUNT_HOLDER_1.getPassportNumber(),
        JOINT_ACCOUNT_HOLDER_1.getDateOfBirth(),
        JOINT_ACCOUNT_HOLDER_1.getEmail()
      ))
      .isInstanceOf(BankAccountNotActiveException.class);
  }

  @Test
  void shouldThrowMaxJointAccountHoldersExceededException_whenJointLimitIsReached() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.addJointAccountHolder(
      JOINT_ACCOUNT_HOLDER_1.getAccountHolderName(),
      JOINT_ACCOUNT_HOLDER_1.getPassportNumber(),
      JOINT_ACCOUNT_HOLDER_1.getDateOfBirth(),
      JOINT_ACCOUNT_HOLDER_1.getEmail()
    );

    assertThatThrownBy(() ->
      bankAccount.addJointAccountHolder(
        JOINT_ACCOUNT_HOLDER_2.getAccountHolderName(),
        JOINT_ACCOUNT_HOLDER_2.getPassportNumber(),
        JOINT_ACCOUNT_HOLDER_2.getDateOfBirth(),
        JOINT_ACCOUNT_HOLDER_2.getEmail()
      ))
      .isInstanceOf(MaxJointAccountHoldersExceededException.class);
  }
}
