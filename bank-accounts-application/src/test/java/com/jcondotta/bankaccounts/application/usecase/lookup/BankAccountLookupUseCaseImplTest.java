package com.jcondotta.bankaccounts.application.usecase.lookup;

import com.jcondotta.bankaccounts.application.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.LookupBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.lookup.mapper.AccountHolderDetailsMapperImpl;
import com.jcondotta.bankaccounts.application.usecase.lookup.mapper.BankAccountDetailsMapper;
import com.jcondotta.bankaccounts.application.usecase.lookup.mapper.BankAccountDetailsMapperImpl;
import com.jcondotta.bankaccounts.application.usecase.lookup.model.BankAccountDetails;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.domain.value_objects.Iban;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountLookupUseCaseImplTest {

  private static final Iban VALID_IBAN =
    Iban.of("ES3801283316232166447417");

  private static final Clock FIXED_CLOCK = ClockTestFactory.FIXED_CLOCK;
  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(FIXED_CLOCK);

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
    BankAccount bankAccount = BankAccount.open(
      AccountHolderFixtures.JEFFERSON.getAccountHolderName(),
      AccountHolderFixtures.JEFFERSON.getPassportNumber(),
      AccountHolderFixtures.JEFFERSON.getDateOfBirth(),
      AccountType.CHECKING,
      Currency.EUR,
      VALID_IBAN,
      CREATED_AT
    );
    BankAccountDetails bankAccountDetails = new BankAccountDetailsMapperImpl(new AccountHolderDetailsMapperImpl()).toDetails(bankAccount);

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
