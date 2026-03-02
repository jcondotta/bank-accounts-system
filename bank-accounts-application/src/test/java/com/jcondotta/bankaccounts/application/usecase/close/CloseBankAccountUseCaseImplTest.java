package com.jcondotta.bankaccounts.application.usecase.close;

import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.application.usecase.close.model.CloseBankAccountCommand;
import com.jcondotta.bankaccounts.domain.enums.AccountStatus;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.repository.BankAccountRepository;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloseBankAccountUseCaseImplTest {

  @Mock
  private BankAccountRepository bankAccountRepository;

  private CloseBankAccountUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new CloseBankAccountUseCaseImpl(bankAccountRepository);
  }

  @Test
  void shouldCloseBankAccount_whenCommandIsValid() {
    var bankAccount = BankAccountTestFixture.openActiveAccount(AccountHolderFixtures.JEFFERSON);
    bankAccount.pullEvents();

    when(bankAccountRepository.findById(bankAccount.id()))
      .thenReturn(Optional.of(bankAccount));

    var command = new CloseBankAccountCommand(bankAccount.id());

    useCase.execute(command);

    assertThat(bankAccount.accountStatus())
      .isEqualTo(AccountStatus.CLOSED);

    verify(bankAccountRepository).findById(bankAccount.id());
    verify(bankAccountRepository).save(bankAccount);
    verifyNoMoreInteractions(bankAccountRepository);
  }

  @Test
  void shouldThrowBankAccountNotFoundException_whenBankAccountDoesNotExist() {
    var bankAccountId = BankAccountId.newId();

    when(bankAccountRepository.findById(bankAccountId))
      .thenReturn(Optional.empty());

    var command = new CloseBankAccountCommand(bankAccountId);

    assertThatThrownBy(() -> useCase.execute(command))
      .isInstanceOf(BankAccountNotFoundException.class);

    verify(bankAccountRepository).findById(bankAccountId);
    verifyNoMoreInteractions(bankAccountRepository);
  }

  @Test
  void shouldThrowNullPointerException_whenCommandIsNull() {

    assertThatThrownBy(() -> useCase.execute(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("command must not be null");

    verifyNoInteractions(bankAccountRepository);
  }
}