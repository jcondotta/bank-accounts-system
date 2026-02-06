package com.jcondotta.bankaccounts.application.usecase.blockbankaccount;

import com.jcondotta.bankaccounts.application.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.application.ports.output.repository.lookupbankaccount.BankAccountLookupRepository;
import com.jcondotta.bankaccounts.application.ports.output.repository.updatebankaccount.BankAccountUpdateRepository;
import com.jcondotta.bankaccounts.application.usecase.blockbankaccount.model.BlockBankAccountCommand;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockBankAccountUseCaseImplTest {

  private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();
  private static final Iban VALID_IBAN = Iban.of("ES3801283316232166447417");

  private static final AccountHolderName ACCOUNT_HOLDER_NAME = AccountHolderName.of("Jefferson Condotta");
  private static final PassportNumber PASSPORT_NUMBER = PassportNumber.of("A1234567");
  private static final DateOfBirth DATE_OF_BIRTH = DateOfBirth.of(LocalDate.of(1988, Month.JUNE, 24));

  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(ClockTestFactory.FIXED_CLOCK);

  @Mock
  private BankAccountLookupRepository bankAccountLookupRepository;

  @Mock
  private BankAccountUpdateRepository bankAccountUpdateRepository;

  private BlockBankAccountUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new BlockBankAccountUseCaseImpl(bankAccountLookupRepository, bankAccountUpdateRepository);
  }

  @Test
  void shouldBlockBankAccount_whenCommandIsValid() {
    BankAccount bankAccount = BankAccount.open(
      ACCOUNT_HOLDER_NAME,
      PASSPORT_NUMBER,
      DATE_OF_BIRTH,
      AccountType.CHECKING,
      Currency.USD,
      VALID_IBAN,
      CREATED_AT);

    bankAccount.activate();

    bankAccount.block();

    when(bankAccountLookupRepository.byId(BANK_ACCOUNT_ID))
      .thenReturn(Optional.of(bankAccount));

    var command = BlockBankAccountCommand.of(BANK_ACCOUNT_ID);
    useCase.execute(command);

    verify(bankAccountUpdateRepository).save(bankAccount);
    verifyNoMoreInteractions(bankAccountLookupRepository, bankAccountUpdateRepository);
  }

  @Test
  void shouldThrowAccountRecipientNotFoundException_whenRecipientDoesNotExist() {
    when(bankAccountLookupRepository.byId(BANK_ACCOUNT_ID)).thenReturn(Optional.empty());

    var command = BlockBankAccountCommand.of(BANK_ACCOUNT_ID);

    assertThatThrownBy(() -> useCase.execute(command))
      .isInstanceOf(BankAccountNotFoundException.class);

    verify(bankAccountLookupRepository).byId(BANK_ACCOUNT_ID);
    verifyNoInteractions(bankAccountUpdateRepository);
    verifyNoMoreInteractions(bankAccountLookupRepository);
  }

  @Test
  void shouldThrowNullPointerException_whenCommandIsNull() {
    assertThatThrownBy(() -> useCase.execute(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("command must not be null");

    verifyNoInteractions(bankAccountLookupRepository, bankAccountUpdateRepository);
  }
}