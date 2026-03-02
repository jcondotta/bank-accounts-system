package com.jcondotta.bankaccounts.application.usecase.addholder;

import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.application.usecase.addholder.model.AddJointAccountHolderCommand;
import com.jcondotta.bankaccounts.domain.aggregates.BankAccount;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.repository.BankAccountRepository;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
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
class AddJointAccountHolderUseCaseImplTest {

  @Mock
  private BankAccountRepository bankAccountRepository;

  private AddJointAccountHolderUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new AddJointAccountHolderUseCaseImpl(bankAccountRepository);
  }

  @Test
  void shouldAddJointAccountHolder_whenCommandIsValid() {
    BankAccount bankAccount = BankAccountTestFixture.openActiveAccount(AccountHolderFixtures.JEFFERSON);

    when(bankAccountRepository.findById(bankAccount.id()))
      .thenReturn(Optional.of(bankAccount));

    var command = new AddJointAccountHolderCommand(
      bankAccount.id(),
      AccountHolderFixtures.VIRGINIO.personalInfo(),
      AccountHolderFixtures.VIRGINIO.contactInfo(),
      AccountHolderFixtures.VIRGINIO.address()
    );

    useCase.execute(command);
    verify(bankAccountRepository).findById(bankAccount.id());
    verify(bankAccountRepository).save(bankAccount);
    verifyNoMoreInteractions(bankAccountRepository);


    assertThat(bankAccount.jointAccountHolders())
      .hasSize(1);
  }

  @Test
  void shouldThrowBankAccountNotFoundException_whenBankAccountDoesNotExist() {
    var bankAccountId = BankAccountId.newId();
    when(bankAccountRepository.findById(bankAccountId))
      .thenReturn(Optional.empty());

    var command = new AddJointAccountHolderCommand(
      bankAccountId,
      AccountHolderFixtures.VIRGINIO.personalInfo(),
      AccountHolderFixtures.VIRGINIO.contactInfo(),
      AccountHolderFixtures.VIRGINIO.address()
    );

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