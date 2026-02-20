package com.jcondotta.bankaccounts.domain.entities;

import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.BankAccountClosedEvent;
import com.jcondotta.bankaccounts.domain.exceptions.InvalidBankAccountStateTransitionException;
import com.jcondotta.bankaccounts.domain.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.domain.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.domain.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountCloseTest {

  private static final AccountHolderFixtures PRIMARY_ACCOUNT_HOLDER = AccountHolderFixtures.JEFFERSON;

  private static final Iban VALID_IBAN = BankAccountTestFixture.VALID_IBAN;
  private static final AccountType ACCOUNT_TYPE_SAVINGS = AccountType.SAVINGS;
  private static final Currency CURRENCY_USD = Currency.USD;

  private static final Instant ACCOUNT_CREATED_AT = Instant.now(ClockTestFactory.FIXED_CLOCK);

  @Test
  void shouldCloseBankAccount_whenStatusIsActive() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);
    bankAccount.pullDomainEvents();

    bankAccount.close();

    assertThat(bankAccount.accountStatus().isClosed()).isTrue();

    var events = bankAccount.pullDomainEvents();

    assertThat(events)
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(BankAccountClosedEvent.class, event -> {
        assertThat(event.eventId()).isNotNull();
        assertThat(event.bankAccountId()).isEqualTo(bankAccount.id());
        assertThat(event.occurredAt()).isNotNull();
      });
  }

  @Test
  void shouldNotThrowAnyException_whenCloseIsCalledTwice() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(PRIMARY_ACCOUNT_HOLDER);

    bankAccount.close();
    bankAccount.close();

    assertThat(bankAccount.accountStatus().isClosed()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(value = AccountStatus.class, names = {"ACTIVE", "CLOSED"}, mode = EnumSource.Mode.EXCLUDE)
  void shouldThrowInvalidBankAccountStateTransitionException_whenClosingFromInvalidState(AccountStatus status) {
    var primaryAccountHolder = BankAccountTestFixture.createPrimaryHolder(PRIMARY_ACCOUNT_HOLDER, ACCOUNT_CREATED_AT);

    var bankAccount = BankAccount.restore(
      BankAccountId.newId(),
      ACCOUNT_TYPE_SAVINGS,
      CURRENCY_USD,
      VALID_IBAN,
      status,
      ACCOUNT_CREATED_AT,
      List.of(primaryAccountHolder)
    );

    assertThatThrownBy(bankAccount::close)
      .isInstanceOf(InvalidBankAccountStateTransitionException.class);
  }
}
