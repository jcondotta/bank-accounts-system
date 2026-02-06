package com.jcondotta.bankaccounts.application.usecase.openbankaccount;

import com.jcondotta.bankaccounts.application.argument_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.application.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.ports.output.IbanGenerator;
import com.jcondotta.bankaccounts.application.ports.output.repository.openbankaccount.OpenBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.openbankaccount.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenBankAccountUseCaseImplTest {

  private static final AccountHolderName ACCOUNT_HOLDER_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber PASSPORT_NUMBER = AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth();

  private static final Iban GENERATED_IBAN = Iban.of("ES3801283316232166447417");

  private static final Clock FIXED_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(FIXED_CLOCK);

  @Mock
  private OpenBankAccountRepository openBankAccountRepository;

  @Mock
  private IbanGenerator ibanGenerator;

  private OpenBankAccountUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new OpenBankAccountUseCaseImpl(openBankAccountRepository, ibanGenerator, FIXED_CLOCK);
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldOpenBankAccount_whenCommandIsValid(AccountType accountType, Currency currency) {
    when(ibanGenerator.generate(accountType, currency))
      .thenReturn(GENERATED_IBAN);

    var command = OpenBankAccountCommand.of(ACCOUNT_HOLDER_NAME, PASSPORT_NUMBER, DATE_OF_BIRTH, accountType, currency);

    useCase.execute(command);

    ArgumentCaptor<BankAccount> bankAccountCaptor = ArgumentCaptor.forClass(BankAccount.class);

    verify(ibanGenerator).generate(accountType, currency);
    verify(openBankAccountRepository).save(bankAccountCaptor.capture());
    verifyNoMoreInteractions(ibanGenerator, openBankAccountRepository);

    assertThat(bankAccountCaptor.getValue())
      .satisfies(bankAccount -> {
        assertThat(bankAccount.getBankAccountId()).isNotNull();
        assertThat(bankAccount.getAccountType()).isEqualTo(accountType);
        assertThat(bankAccount.getCurrency()).isEqualTo(currency);
        assertThat(bankAccount.getIban()).isEqualTo(GENERATED_IBAN);
        assertThat(bankAccount.getStatus()).isEqualTo(BankAccount.ACCOUNT_STATUS_ON_OPENING);
        assertThat(bankAccount.getCreatedAt()).isEqualTo(CREATED_AT);
        assertThat(bankAccount.getAccountHolders())
          .hasSize(1)
          .singleElement()
          .satisfies(accountHolder -> {
            assertThat(accountHolder.getAccountHolderId()).isNotNull();
            assertThat(accountHolder.getAccountHolderName()).isEqualTo(ACCOUNT_HOLDER_NAME);
            assertThat(accountHolder.getPassportNumber()).isEqualTo(PASSPORT_NUMBER);
            assertThat(accountHolder.getDateOfBirth()).isEqualTo(DATE_OF_BIRTH);
            assertThat(accountHolder.isPrimaryAccountHolder()).isTrue();
            assertThat(accountHolder.getCreatedAt()).isEqualTo(CREATED_AT);
          });
      });
  }

  @Test
  void shouldThrowNullPointerException_whenCommandIsNull() {
    assertThatThrownBy(() -> useCase.execute(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("command must not be null");

    verifyNoInteractions(openBankAccountRepository, ibanGenerator);
  }
}
