package com.jcondotta.bankaccounts.application.usecase.activatebankaccount;

import com.jcondotta.bankaccounts.application.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.application.ports.output.messaging.DomainEventPublisher;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.lookupbankaccount.BankAccountLookupRepository;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.updatebankaccount.BankAccountUpdateRepository;
import com.jcondotta.bankaccounts.application.usecase.activatebankaccount.model.ActivateBankAccountCommand;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.BankAccountActivatedEvent;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivateBankAccountUseCaseImplTest {

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

  @Mock
  private DomainEventPublisher domainEventPublisher;

  @Captor
  private ArgumentCaptor<DomainEvent> eventArgumentCaptor;

  private ActivateBankAccountUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new ActivateBankAccountUseCaseImpl(
      bankAccountLookupRepository,
      bankAccountUpdateRepository,
      domainEventPublisher
    );
  }

  @Test
  void shouldActivateBankAccount_whenCommandIsValid() {
    BankAccount bankAccount = BankAccount.open(
      ACCOUNT_HOLDER_NAME,
      PASSPORT_NUMBER,
      DATE_OF_BIRTH,
      AccountType.CHECKING,
      Currency.USD,
      VALID_IBAN,
      CREATED_AT);

    bankAccount.pullDomainEvents();

    bankAccount.activate();

    when(bankAccountLookupRepository.byId(BANK_ACCOUNT_ID))
      .thenReturn(Optional.of(bankAccount));

    var command = new ActivateBankAccountCommand(BANK_ACCOUNT_ID);
    useCase.execute(command);

    verify(bankAccountUpdateRepository).save(bankAccount);
    verify(domainEventPublisher).publish(eventArgumentCaptor.capture());
    verifyNoMoreInteractions(bankAccountLookupRepository, bankAccountUpdateRepository);

    assertThat(eventArgumentCaptor.getAllValues())
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(BankAccountActivatedEvent.class, event -> {
          assertThat(event.bankAccountId()).isEqualTo(bankAccount.getBankAccountId());
          assertThat(event.occurredAt()).isEqualTo(CREATED_AT);
        }
      );
  }

  @Test
  void shouldThrowAccountRecipientNotFoundException_whenRecipientDoesNotExist() {
    when(bankAccountLookupRepository.byId(BANK_ACCOUNT_ID)).thenReturn(Optional.empty());

    var command = new ActivateBankAccountCommand(BANK_ACCOUNT_ID);

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