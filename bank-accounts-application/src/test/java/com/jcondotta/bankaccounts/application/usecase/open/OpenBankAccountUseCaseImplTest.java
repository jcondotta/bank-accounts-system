package com.jcondotta.bankaccounts.application.usecase.open;

import com.jcondotta.bankaccounts.application.argument_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.application.command.open.OpenBankAccountUseCase;
import com.jcondotta.bankaccounts.application.command.open.OpenBankAccountUseCaseImpl;
import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.application.ports.output.facade.IbanGeneratorFacade;
import com.jcondotta.bankaccounts.application.command.open.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.domain.aggregates.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.repository.BankAccountRepository;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenBankAccountUseCaseImplTest {

  private static final Iban GENERATED_IBAN = BankAccountTestFixture.VALID_IBAN;

  @Mock
  private BankAccountRepository bankAccountRepository;

  @Mock
  private IbanGeneratorFacade ibanGeneratorFacade;

  @Captor
  private ArgumentCaptor<BankAccount> bankAccountCaptor;

  private OpenBankAccountUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new OpenBankAccountUseCaseImpl(
      bankAccountRepository,
      ibanGeneratorFacade
    );
  }

  @ParameterizedTest
  @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
  void shouldOpenBankAccount_whenCommandIsValid(AccountType accountType, Currency currency) {
    when(ibanGeneratorFacade.generate()).thenReturn(GENERATED_IBAN);

    var personalInfo = AccountHolderFixtures.JEFFERSON.personalInfo();
    var contactInfo = AccountHolderFixtures.JEFFERSON.contactInfo();
    var address = AccountHolderFixtures.JEFFERSON.address();

    var command = new OpenBankAccountCommand(
      personalInfo,
      contactInfo,
      address,
      accountType,
      currency
    );

    useCase.execute(command);

    verify(ibanGeneratorFacade).generate();
    verify(bankAccountRepository).save(bankAccountCaptor.capture());
    verifyNoMoreInteractions(ibanGeneratorFacade, bankAccountRepository);

    assertThat(bankAccountCaptor.getValue())
      .satisfies(bankAccount -> {
        assertThat(bankAccount.getId()).isNotNull();
        assertThat(bankAccount.getAccountType()).isEqualTo(accountType);
        assertThat(bankAccount.getCurrency()).isEqualTo(currency);
        assertThat(bankAccount.getIban()).isEqualTo(GENERATED_IBAN);
        assertThat(bankAccount.getAccountStatus()).isEqualTo(BankAccount.ACCOUNT_STATUS_ON_OPENING);
        assertThat(bankAccount.getCreatedAt()).isNotNull();
        assertThat(bankAccount.getAccountHolders())
          .hasSize(1)
          .singleElement()
          .satisfies(accountHolder -> {
            assertThat(accountHolder.getId()).isNotNull();

            assertThat(accountHolder.getPersonalInfo()).isEqualTo(personalInfo);
            assertThat(accountHolder.getContactInfo()).isEqualTo(contactInfo);
            assertThat(accountHolder.getAddress()).isEqualTo(address);
            assertThat(accountHolder.isPrimary()).isTrue();
            assertThat(accountHolder.getCreatedAt()).isNotNull();
          });
      });
  }

  @Test
  void shouldThrowNullPointerException_whenCommandIsNull() {
    assertThatThrownBy(() -> useCase.execute(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("command must not be null");

    verifyNoInteractions(bankAccountRepository, ibanGeneratorFacade);
  }
}
