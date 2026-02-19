package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.arguments_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.*;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotActiveException;
import com.jcondotta.bankaccounts.domain.exceptions.InvalidBankAccountStateTransitionException;
import com.jcondotta.bankaccounts.domain.exceptions.MaxJointAccountHoldersExceededException;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;
  private static final AccountHolderFixtures JOINT_ACCOUNT_HOLDER = AccountHolderFixtures.PATRIZIO;

  private static final AccountHolderName JOINT_ACCOUNT_HOLDER_NAME = AccountHolderFixtures.PATRIZIO.getAccountHolderName();
  private static final PassportNumber JOINT_PASSPORT_NUMBER = AccountHolderFixtures.PATRIZIO.getPassportNumber();
  private static final DateOfBirth JOINT_DATE_OF_BIRTH = AccountHolderFixtures.PATRIZIO.getDateOfBirth();
  private static final Email JOINT_EMAIL = AccountHolderFixtures.PATRIZIO.getEmail();

  private static final Clock FIXED_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final Instant CREATED_AT = Instant.now(ClockTestFactory.FIXED_CLOCK);

  private static final Clock OCCURRED_AT =
    Clock.fixed(CREATED_AT.plus(2, ChronoUnit.HOURS), ZoneOffset.UTC);

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldOpenBankAccountWithPrimaryHolder_whenValidDataProvided(AccountType accountType, Currency currency) {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER, accountType, currency, FIXED_CLOCK);

    assertThat(bankAccount)
      .satisfies(account -> {
        assertThat(account.id()).isNotNull();
        assertThat(account.accountType()).isEqualTo(accountType);
        assertThat(account.currency()).isEqualTo(currency);
        assertThat(account.iban()).isEqualTo(BankAccountTestFixture.VALID_IBAN);
        assertThat(account.accountStatus()).isEqualTo(BankAccount.ACCOUNT_STATUS_ON_OPENING);
        assertThat(account.createdAt()).isEqualTo(Instant.now(FIXED_CLOCK));
        assertThat(account.accountHolders())
          .hasSize(1)
          .first()
          .satisfies(accountHolder -> {
            assertThat(accountHolder.id()).isNotNull();
            assertThat(accountHolder.name()).isEqualTo(PRIMARY_ACCOUNT_HOLDER.getAccountHolderName());
            assertThat(accountHolder.passportNumber()).isEqualTo(PRIMARY_ACCOUNT_HOLDER.getPassportNumber());
            assertThat(accountHolder.dateOfBirth()).isEqualTo(PRIMARY_ACCOUNT_HOLDER.getDateOfBirth());
            assertThat(accountHolder.isPrimary()).isTrue();
            assertThat(accountHolder.createdAt()).isEqualTo(Instant.now(FIXED_CLOCK));
          });

        var events = bankAccount.pullDomainEvents();
        var primaryAccountHolder = account.primaryAccountHolder();

        assertThat(events)
          .hasSize(1)
          .singleElement()
          .isInstanceOfSatisfying(BankAccountOpenedEvent.class, event -> {
              assertThat(event.eventId()).isNotNull();
              assertThat(event.bankAccountId()).isEqualTo(bankAccount.id());
              assertThat(event.accountType()).isEqualTo(accountType);
              assertThat(event.currency()).isEqualTo(currency);
              assertThat(event.primaryAccountHolderId()).isEqualTo(primaryAccountHolder.id());
              assertThat(event.occurredAt()).isEqualTo(Instant.now(FIXED_CLOCK));
            }
          );
      });
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldAddJointAccountHolder_whenBankAccountIsActive(AccountType accountType, Currency currency) {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER, accountType, currency, FIXED_CLOCK);

    bankAccount.addJointAccountHolder(JOINT_ACCOUNT_HOLDER_NAME, JOINT_PASSPORT_NUMBER, JOINT_DATE_OF_BIRTH, JOINT_EMAIL, FIXED_CLOCK);

    assertThat(bankAccount.accountHolders())
      .hasSize(2)
      .filteredOn(AccountHolder::isJoint)
      .hasSize(1)
      .singleElement()
      .satisfies(holder -> {
        assertThat(holder.id()).isNotNull();
        assertThat(holder.name()).isEqualTo(JOINT_ACCOUNT_HOLDER_NAME);
        assertThat(holder.passportNumber()).isEqualTo(JOINT_PASSPORT_NUMBER);
        assertThat(holder.dateOfBirth()).isEqualTo(JOINT_DATE_OF_BIRTH);
        assertThat(holder.email()).isEqualTo(JOINT_EMAIL);
        assertThat(holder.isJoint()).isTrue();
        assertThat(holder.createdAt()).isEqualTo(Instant.now(FIXED_CLOCK));
      });

    var events = bankAccount.pullDomainEvents();
    var jointAccountHolder = bankAccount.jointAccountHolders().getFirst();

    assertThat(events)
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(JointAccountHolderAddedEvent.class, event -> {
          assertThat(event.eventId()).isNotNull();
          assertThat(event.bankAccountId()).isEqualTo(bankAccount.id());
          assertThat(event.accountHolderId()).isEqualTo(jointAccountHolder.id());
          assertThat(event.occurredAt()).isEqualTo(Instant.now(FIXED_CLOCK));
        }
      );
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldRestoreBankAccountWithPrimaryAccountHolder_whenValidDataProvided(AccountType accountType, Currency currency) {
    var primaryAccountHolderId = AccountHolderId.newId();
    var primaryAccountHolder = BankAccount.restoreAccountHolder(
      primaryAccountHolderId,
      PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
      PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
      PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
      PRIMARY_ACCOUNT_HOLDER.getEmail(),
      AccountHolderType.PRIMARY,
      CREATED_AT
    );

    var bankAccountId = BankAccountId.newId();
    var bankAccount = BankAccount.restore(
      bankAccountId,
      accountType,
      currency,
      BankAccountTestFixture.VALID_IBAN,
      AccountStatus.ACTIVE,
      CREATED_AT,
      List.of(primaryAccountHolder)
    );

    assertThat(bankAccount).isNotNull();
    assertThat(bankAccount.id()).isEqualTo(bankAccountId);
    assertThat(bankAccount.accountType()).isEqualTo(accountType);
    assertThat(bankAccount.currency()).isEqualTo(currency);
    assertThat(bankAccount.iban()).isEqualTo(BankAccountTestFixture.VALID_IBAN);
    assertThat(bankAccount.accountStatus().isActive()).isTrue();
    assertThat(bankAccount.createdAt()).isEqualTo(CREATED_AT);
    assertThat(bankAccount.pullDomainEvents()).isEmpty();

    assertThat(bankAccount.accountHolders())
      .hasSize(1)
      .containsExactlyInAnyOrder(primaryAccountHolder);
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldRestoreBankAccountWithPrimaryAndJointAccountHolders_whenValidDataProvided(AccountType accountType, Currency currency) {
    var primaryAccountHolderId = AccountHolderId.newId();
    var primaryAccountHolder = BankAccount.restoreAccountHolder(
      primaryAccountHolderId,
      PRIMARY_ACCOUNT_HOLDER.getAccountHolderName(),
      PRIMARY_ACCOUNT_HOLDER.getPassportNumber(),
      PRIMARY_ACCOUNT_HOLDER.getDateOfBirth(),
      PRIMARY_ACCOUNT_HOLDER.getEmail(),
      AccountHolderType.PRIMARY,
      CREATED_AT
    );

    var jointAccountHolderId = AccountHolderId.newId();
    var jointAccountHolder = BankAccount.restoreAccountHolder(
      jointAccountHolderId,
      JOINT_ACCOUNT_HOLDER_NAME,
      JOINT_PASSPORT_NUMBER,
      JOINT_DATE_OF_BIRTH,
      JOINT_EMAIL,
      AccountHolderType.JOINT,
      CREATED_AT
    );

    var bankAccountId = BankAccountId.newId();
    var bankAccount = BankAccount.restore(
      bankAccountId,
      accountType,
      currency,
      BankAccountTestFixture.VALID_IBAN,
      AccountStatus.ACTIVE,
      CREATED_AT,
      List.of(primaryAccountHolder, jointAccountHolder)
    );

    assertThat(bankAccount).isNotNull();
    assertThat(bankAccount.id()).isEqualTo(bankAccountId);
    assertThat(bankAccount.accountType()).isEqualTo(accountType);
    assertThat(bankAccount.currency()).isEqualTo(currency);
    assertThat(bankAccount.iban()).isEqualTo(BankAccountTestFixture.VALID_IBAN);
    assertThat(bankAccount.accountStatus().isActive()).isTrue();
    assertThat(bankAccount.createdAt()).isEqualTo(CREATED_AT);
    assertThat(bankAccount.pullDomainEvents()).isEmpty();

    assertThat(bankAccount.accountHolders())
      .hasSize(2)
      .containsExactlyInAnyOrder(primaryAccountHolder, jointAccountHolder);
  }

  @Test
  void shouldThrowBankAccountNotActiveException_whenAccountIsNotActive() {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);

    assertThatThrownBy(() -> addValidJointAccountHolder(bankAccount, JOINT_ACCOUNT_HOLDER))
      .isInstanceOf(BankAccountNotActiveException.class);
  }

  @Test
  void shouldThrowMaxJointAccountHoldersExceededException_whenJointAccountHoldersLimitIsReached() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    addValidJointAccountHolder(bankAccount, JOINT_ACCOUNT_HOLDER);

    assertThatThrownBy(() -> addValidJointAccountHolder(bankAccount, AccountHolderFixtures.VIRGINIO))
      .isInstanceOf(MaxJointAccountHoldersExceededException.class);
  }

  @Test
  void shouldActivateBankAccount_whenStatusIsPending() {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);
    bankAccount.pullDomainEvents();

    bankAccount.activate(OCCURRED_AT);
    assertThat(bankAccount.accountStatus()).isEqualTo(AccountStatus.ACTIVE);

    var events = bankAccount.pullDomainEvents();

    assertThat(events)
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(BankAccountActivatedEvent.class, event -> {
          assertThat(event.bankAccountId()).isEqualTo(bankAccount.id());
          assertThat(event.occurredAt()).isEqualTo(Instant.now(OCCURRED_AT));
        }
      );
  }

  @Test
  void shouldNotThrowAnyException_whenActivateIsCalledTwice() {
    var bankAccount = BankAccountTestFixture.openPendingAccount(
      PRIMARY_ACCOUNT_HOLDER,
      AccountType.CHECKING,
      Currency.USD,
      FIXED_CLOCK
    );

    bankAccount.activate(OCCURRED_AT);
    bankAccount.activate(OCCURRED_AT);

    assertThat(bankAccount.accountStatus().isActive()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(value = AccountStatus.class, names = {"PENDING", "ACTIVE"}, mode = EnumSource.Mode.EXCLUDE)
  void shouldThrowInvalidBankAccountStateTransitionException_whenActivatingFromInvalidState(AccountStatus status) {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);
    ReflectionTestUtils.setField(bankAccount, "accountStatus", status);

    assertThatThrownBy(() -> bankAccount.activate(OCCURRED_AT))
      .isInstanceOf(InvalidBankAccountStateTransitionException.class);
  }

  @Test
  void shouldBlockBankAccount_whenStatusIsActive() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.block(OCCURRED_AT);
    assertThat(bankAccount.accountStatus().isBlocked()).isTrue();

    var events = bankAccount.pullDomainEvents();

    assertThat(events)
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(BankAccountBlockedEvent.class, event -> {
          assertThat(event.bankAccountId()).isEqualTo(bankAccount.id());
          assertThat(event.occurredAt()).isEqualTo(Instant.now(OCCURRED_AT));
        }
      );
  }

  @Test
  void shouldNotThrowAnyException_whenBlockIsCalledTwice() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.block(OCCURRED_AT);
    bankAccount.block(OCCURRED_AT);

    assertThat(bankAccount.accountStatus().isBlocked()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(value = AccountStatus.class, names = {"BLOCKED", "ACTIVE"}, mode = EnumSource.Mode.EXCLUDE)
  void shouldThrowInvalidBankAccountStateTransitionException_whenBlockingFromInvalidState(AccountStatus status) {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);
    ReflectionTestUtils.setField(bankAccount, "accountStatus", status);

    assertThatThrownBy(() -> bankAccount.block(OCCURRED_AT))
      .isInstanceOf(InvalidBankAccountStateTransitionException.class);
  }

  @Test
  void shouldUnblockBankAccount_whenStatusIsBlocked() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.block(OCCURRED_AT);
    bankAccount.pullDomainEvents();

    bankAccount.unblock(OCCURRED_AT);

    assertThat(bankAccount.accountStatus().isActive()).isTrue();

    var events = bankAccount.pullDomainEvents();

    assertThat(events)
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(BankAccountUnblockedEvent.class, event -> {
          assertThat(event.bankAccountId()).isEqualTo(bankAccount.id());
        assertThat(event.occurredAt()).isEqualTo(Instant.now(OCCURRED_AT));
        }
      );
  }

  @Test
  void shouldNotThrowAnyException_whenUnblockIsCalledTwice() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.block(OCCURRED_AT);
    bankAccount.unblock(OCCURRED_AT);
    bankAccount.unblock(OCCURRED_AT);

    assertThat(bankAccount.accountStatus().isActive()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(value = AccountStatus.class, names = {"BLOCKED", "ACTIVE"}, mode = EnumSource.Mode.EXCLUDE)
  void shouldThrowInvalidBankAccountStateTransitionException_whenUnblockingFromInvalidState(AccountStatus status) {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);

    ReflectionTestUtils.setField(bankAccount, "accountStatus", status);

    assertThatThrownBy(() -> bankAccount.unblock(OCCURRED_AT))
      .isInstanceOf(InvalidBankAccountStateTransitionException.class);
  }

  @Test
  void shouldCloseBankAccount_whenStatusIsActive() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);
    bankAccount.close(OCCURRED_AT);

    assertThat(bankAccount.accountStatus()).isEqualTo(AccountStatus.CLOSED);

    var events = bankAccount.pullDomainEvents();

    assertThat(events)
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(BankAccountClosedEvent.class, event -> {
          assertThat(event.eventId()).isNotNull();
          assertThat(event.bankAccountId()).isEqualTo(bankAccount.id());
          assertThat(event.occurredAt()).isEqualTo(Instant.now(OCCURRED_AT));
        }
      );
  }

  @Test
  void shouldNotThrowAnyException_whenCloseIsCalledTwice() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.close(OCCURRED_AT);
    bankAccount.close(OCCURRED_AT);

    assertThat(bankAccount.accountStatus()).isEqualTo(AccountStatus.CLOSED);
  }

  @ParameterizedTest
  @EnumSource(value = AccountStatus.class, names = {"BLOCKED", "CLOSED", "ACTIVE"}, mode = EnumSource.Mode.EXCLUDE)
  void shouldThrowInvalidBankAccountStateTransitionException_whenClosingFromInvalidState(AccountStatus status) {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);

    ReflectionTestUtils.setField(bankAccount, "accountStatus", status);

    assertThatThrownBy(() -> bankAccount.close(OCCURRED_AT))
      .isInstanceOf(InvalidBankAccountStateTransitionException.class);
  }

  @Test
  void shouldReturnAccountHolder_whenAccountHolderIsFound() {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);

    var primaryAccountHolder = bankAccount.primaryAccountHolder();

    assertThat(bankAccount.findAccountHolder(primaryAccountHolder.id()))
      .hasValueSatisfying(holder -> assertThat(holder).isSameAs(primaryAccountHolder));
  }

  @Test
  void shouldReturnOnlyJointAccountHolders_whenJointAccountHoldersExist() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);
    addValidJointAccountHolder(bankAccount, JOINT_ACCOUNT_HOLDER);

    assertThat(bankAccount.jointAccountHolders())
      .hasSize(1);
  }

  @Test
  void shouldReturnEmptyJointAccountHolders_whenNoJointAccountHoldersExist() {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);
    assertThat(bankAccount.jointAccountHolders()).isEmpty();
  }

  @Test
  void shouldReturnEmpty_whenAccountHolderIsNotFound() {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);
    assertThat(bankAccount.findAccountHolder(AccountHolderId.newId())).isEmpty();
  }

  @Test
  void shouldReturnEmpty_whenAccountHolderIdIsNull() {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);
    assertThat(bankAccount.findAccountHolder(null)).isEmpty();
  }

  private void addValidJointAccountHolder(BankAccount bankAccount, AccountHolderFixtures holder) {
    bankAccount.addJointAccountHolder(
      holder.getAccountHolderName(),
      holder.getPassportNumber(),
      holder.getDateOfBirth(),
      holder.getEmail(),
      FIXED_CLOCK
    );
  }
}