package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.arguments_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.BankAccountActivatedEvent;
import com.jcondotta.bankaccounts.domain.events.BankAccountBlockedEvent;
import com.jcondotta.bankaccounts.domain.events.BankAccountOpenedEvent;
import com.jcondotta.bankaccounts.domain.events.JointAccountHolderAddedEvent;
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
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;
  private static final AccountHolderFixtures JOINT_ACCOUNT_HOLDER = AccountHolderFixtures.PATRIZIO;

  private static final AccountHolderName JOINT_ACCOUNT_HOLDER_NAME = AccountHolderFixtures.PATRIZIO.getAccountHolderName();
  private static final PassportNumber JOINT_PASSPORT_NUMBER = AccountHolderFixtures.PATRIZIO.getPassportNumber();
  private static final DateOfBirth JOINT_DATE_OF_BIRTH = AccountHolderFixtures.PATRIZIO.getDateOfBirth();

  private static final Clock FIXED_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(ClockTestFactory.FIXED_CLOCK);

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldOpenBankAccountWithPrimaryHolder_whenValidDataProvided(AccountType accountType, Currency currency) {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER, accountType, currency, FIXED_CLOCK);

    assertThat(bankAccount)
      .satisfies(account -> {
        assertThat(account.getBankAccountId()).isNotNull();
        assertThat(account.getAccountType()).isEqualTo(accountType);
        assertThat(account.getCurrency()).isEqualTo(currency);
        assertThat(account.getIban()).isEqualTo(BankAccountTestFixture.VALID_IBAN);
        assertThat(account.getAccountStatus()).isEqualTo(BankAccount.ACCOUNT_STATUS_ON_OPENING);
        assertThat(account.getCreatedAt()).isEqualTo(ZonedDateTime.now(FIXED_CLOCK));
        assertThat(account.getAccountHolders())
          .hasSize(1)
          .first()
          .satisfies(accountHolder -> {
            assertThat(accountHolder.getAccountHolderId()).isNotNull();
            assertThat(accountHolder.getAccountHolderName()).isEqualTo(PRIMARY_ACCOUNT_HOLDER.getAccountHolderName());
            assertThat(accountHolder.getPassportNumber()).isEqualTo(PRIMARY_ACCOUNT_HOLDER.getPassportNumber());
            assertThat(accountHolder.getDateOfBirth()).isEqualTo(PRIMARY_ACCOUNT_HOLDER.getDateOfBirth());
            assertThat(accountHolder.isPrimaryAccountHolder()).isTrue();
            assertThat(accountHolder.getCreatedAt()).isEqualTo(ZonedDateTime.now(FIXED_CLOCK));
          });

        var events = bankAccount.pullDomainEvents();
        var primaryAccountHolder = account.primaryAccountHolder();

        assertThat(events)
          .hasSize(1)
          .singleElement()
          .isInstanceOfSatisfying(BankAccountOpenedEvent.class, event -> {
              assertThat(event.eventId()).isNotNull();
              assertThat(event.bankAccountId()).isEqualTo(bankAccount.getBankAccountId());
              assertThat(event.accountType()).isEqualTo(accountType);
              assertThat(event.currency()).isEqualTo(currency);
              assertThat(event.primaryAccountHolderId()).isEqualTo(primaryAccountHolder.getAccountHolderId());
              assertThat(event.occurredAt()).isEqualTo(ZonedDateTime.now(FIXED_CLOCK));
            }
          );
      });
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldAddJointAccountHolder_whenBankAccountIsActive(AccountType accountType, Currency currency) {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER, accountType, currency, FIXED_CLOCK);

    bankAccount.addJointAccountHolder(JOINT_ACCOUNT_HOLDER_NAME, JOINT_PASSPORT_NUMBER, JOINT_DATE_OF_BIRTH, CREATED_AT);

    assertThat(bankAccount.getAccountHolders())
      .hasSize(2)
      .filteredOn(AccountHolder::isJointAccountHolder)
      .hasSize(1)
      .singleElement()
      .satisfies(holder -> {
        assertThat(holder.getAccountHolderId()).isNotNull();
        assertThat(holder.getAccountHolderName()).isEqualTo(JOINT_ACCOUNT_HOLDER_NAME);
        assertThat(holder.getPassportNumber()).isEqualTo(JOINT_PASSPORT_NUMBER);
        assertThat(holder.getDateOfBirth()).isEqualTo(JOINT_DATE_OF_BIRTH);
        assertThat(holder.isJointAccountHolder()).isTrue();
        assertThat(holder.getCreatedAt()).isEqualTo(ZonedDateTime.now(FIXED_CLOCK));
      });

    var events = bankAccount.pullDomainEvents();
    var jointAccountHolder = bankAccount.jointAccountHolders().getFirst();

    assertThat(events)
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(JointAccountHolderAddedEvent.class, event -> {
          assertThat(event.eventId()).isNotNull();
          assertThat(event.bankAccountId()).isEqualTo(bankAccount.getBankAccountId());
          assertThat(event.accountHolderId()).isEqualTo(jointAccountHolder.getAccountHolderId());
          assertThat(event.occurredAt()).isEqualTo(ZonedDateTime.now(FIXED_CLOCK));
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
    assertThat(bankAccount.getBankAccountId()).isEqualTo(bankAccountId);
    assertThat(bankAccount.getAccountType()).isEqualTo(accountType);
    assertThat(bankAccount.getCurrency()).isEqualTo(currency);
    assertThat(bankAccount.getIban()).isEqualTo(BankAccountTestFixture.VALID_IBAN);
    assertThat(bankAccount.getAccountStatus().isActive()).isTrue();
    assertThat(bankAccount.getCreatedAt()).isEqualTo(CREATED_AT);
    assertThat(bankAccount.pullDomainEvents()).isEmpty();

    assertThat(bankAccount.getAccountHolders())
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
      AccountHolderType.PRIMARY,
      CREATED_AT
    );

    var jointAccountHolderId = AccountHolderId.newId();
    var jointAccountHolder = BankAccount.restoreAccountHolder(
      jointAccountHolderId,
      JOINT_ACCOUNT_HOLDER_NAME,
      JOINT_PASSPORT_NUMBER,
      JOINT_DATE_OF_BIRTH,
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
    assertThat(bankAccount.getBankAccountId()).isEqualTo(bankAccountId);
    assertThat(bankAccount.getAccountType()).isEqualTo(accountType);
    assertThat(bankAccount.getCurrency()).isEqualTo(currency);
    assertThat(bankAccount.getIban()).isEqualTo(BankAccountTestFixture.VALID_IBAN);
    assertThat(bankAccount.getAccountStatus().isActive()).isTrue();
    assertThat(bankAccount.getCreatedAt()).isEqualTo(CREATED_AT);
    assertThat(bankAccount.pullDomainEvents()).isEmpty();

    assertThat(bankAccount.getAccountHolders())
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

    bankAccount.activate();
    assertThat(bankAccount.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);

    var events = bankAccount.pullDomainEvents();

    assertThat(events)
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(BankAccountActivatedEvent.class, event -> {
          assertThat(event.bankAccountId()).isEqualTo(bankAccount.getBankAccountId());
          assertThat(event.occurredAt()).isEqualTo(CREATED_AT);
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

    bankAccount.activate();
    bankAccount.activate();

    assertThat(bankAccount.getAccountStatus().isActive()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(value = AccountStatus.class, names = {"PENDING", "ACTIVE"}, mode = EnumSource.Mode.EXCLUDE)
  void shouldThrowInvalidBankAccountStateTransitionException_whenActivatingFromInvalidState(AccountStatus status) {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);
    ReflectionTestUtils.setField(bankAccount, "accountStatus", status);

    assertThatThrownBy(bankAccount::activate)
      .isInstanceOf(InvalidBankAccountStateTransitionException.class);
  }

  @Test
  void shouldBlockBankAccount_whenStatusIsActive() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.block();
    assertThat(bankAccount.getAccountStatus().isBlocked()).isTrue();

    var events = bankAccount.pullDomainEvents();

    assertThat(events)
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(BankAccountBlockedEvent.class, event -> {
          assertThat(event.bankAccountId()).isEqualTo(bankAccount.getBankAccountId());
          assertThat(event.occurredAt()).isEqualTo(CREATED_AT);
        }
      );
  }

  @Test
  void shouldNotThrowAnyException_whenBlockIsCalledTwice() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.block();
    bankAccount.block();

    assertThat(bankAccount.getAccountStatus().isBlocked()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(value = AccountStatus.class, names = {"BLOCKED", "ACTIVE"}, mode = EnumSource.Mode.EXCLUDE)
  void shouldThrowInvalidBankAccountStateTransitionException_whenBlockingFromInvalidState(AccountStatus status) {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);
    ReflectionTestUtils.setField(bankAccount, "accountStatus", status);

    assertThatThrownBy(bankAccount::block)
      .isInstanceOf(InvalidBankAccountStateTransitionException.class);
  }

  @Test
  void shouldReturnAccountHolder_whenAccountHolderIsFound() {
    var bankAccount = BankAccountTestFixture.openPendingAccount(PRIMARY_ACCOUNT_HOLDER);

    var primaryAccountHolder = bankAccount.primaryAccountHolder();

    assertThat(bankAccount.findAccountHolder(primaryAccountHolder.getAccountHolderId()))
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
      CREATED_AT
    );
  }
}