package com.jcondotta.bankaccounts.application.usecase.lookupbankaccount;

import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.LookupBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.mapper.BankAccountDetailsMapper;
import com.jcondotta.bankaccounts.application.usecase.lookupbankaccount.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
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
  private LookupBankAccountRepository lookupBankAccountRepository;

  @Mock
  private BankAccountDetailsMapper bankAccountDetailsMapper;

  private BankAccountLookupUseCaseImpl useCase;

  @BeforeEach
  void setUp() {
    useCase = new BankAccountLookupUseCaseImpl(
      lookupBankAccountRepository,
      bankAccountDetailsMapper
    );
  }

  @Test
  void shouldReturnBankAccountLookupResult_whenBankAccountExists() {
    BankAccountId bankAccountId = BankAccountId.newId();
    BankAccount bankAccount = mock(BankAccount.class);
    BankAccountDetails bankAccountDetails = mock(BankAccountDetails.class);

    when(lookupBankAccountRepository.byId(bankAccountId)).thenReturn(Optional.of(bankAccount));

    when(bankAccountDetailsMapper.toDetails(bankAccount)).thenReturn(bankAccountDetails);

    BankAccountDetails lookupResult = useCase.lookup(bankAccountId);

    assertThat(lookupResult).isEqualTo(bankAccountDetails);

    verify(lookupBankAccountRepository).byId(bankAccountId);
    verify(bankAccountDetailsMapper).toDetails(bankAccount);
    verifyNoMoreInteractions(lookupBankAccountRepository, bankAccountDetailsMapper);
  }

  @Test
  void shouldThrowBankAccountNotFoundException_whenBankAccountDoesNotExist() {
    BankAccountId bankAccountId = BankAccountId.newId();

    when(lookupBankAccountRepository.byId(bankAccountId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.lookup(bankAccountId))
      .isInstanceOf(BankAccountNotFoundException.class)
      .hasMessageContaining(bankAccountId.value().toString());

    verify(lookupBankAccountRepository).byId(bankAccountId);
    verifyNoInteractions(bankAccountDetailsMapper);
  }
}
