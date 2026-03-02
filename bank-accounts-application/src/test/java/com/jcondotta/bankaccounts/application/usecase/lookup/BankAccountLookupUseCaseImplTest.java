package com.jcondotta.bankaccounts.application.usecase.lookup;

import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.fixtures.BankAccountTestFixture;
import com.jcondotta.bankaccounts.application.usecase.lookup.mapper.BankAccountDetailsMapper;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
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
class BankAccountLookupUseCaseImplTest {

  @Mock
  private BankAccountRepository bankAccountRepository;

  @Mock
  private BankAccountDetailsMapper bankAccountDetailsMapper;

  private BankAccountLookupUseCaseImpl useCase;

  @BeforeEach
  void setUp() {
    useCase = new BankAccountLookupUseCaseImpl(
      bankAccountRepository,
      bankAccountDetailsMapper
    );
  }

  @Test
  void shouldReturnBankAccountDetails_whenBankAccountExists() {
    var bankAccount = BankAccountTestFixture.openPendingAccount(AccountHolderFixtures.JEFFERSON);

    var bankAccountId = bankAccount.id();

    var expectedDetails = mock(BankAccountDetails.class);

    when(bankAccountRepository.findById(bankAccountId))
      .thenReturn(Optional.of(bankAccount));

    when(bankAccountDetailsMapper.toDetails(bankAccount))
      .thenReturn(expectedDetails);

    var result = useCase.lookup(bankAccountId);

    assertThat(result).isEqualTo(expectedDetails);

    verify(bankAccountRepository).findById(bankAccountId);
    verify(bankAccountDetailsMapper).toDetails(bankAccount);
    verifyNoMoreInteractions(bankAccountRepository, bankAccountDetailsMapper);
  }

  @Test
  void shouldThrowBankAccountNotFoundException_whenBankAccountDoesNotExist() {
    var bankAccountId = BankAccountId.newId();

    when(bankAccountRepository.findById(bankAccountId))
      .thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.lookup(bankAccountId))
      .isInstanceOf(BankAccountNotFoundException.class)
      .hasMessageContaining(bankAccountId.value().toString());

    verify(bankAccountRepository).findById(bankAccountId);
    verifyNoInteractions(bankAccountDetailsMapper);
  }
}