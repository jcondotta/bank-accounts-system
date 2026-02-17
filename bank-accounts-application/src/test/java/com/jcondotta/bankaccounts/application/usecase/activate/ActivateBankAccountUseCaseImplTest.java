package com.jcondotta.bankaccounts.application.usecase.activate;

import com.jcondotta.bankaccounts.application.factory.ClockTestFactory;
import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.ports.output.messaging.BankAccountActivatedEventPublisher;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.LookupBankAccountRepository;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.UpdateBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.activate.model.ActivateBankAccountCommand;
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

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivateBankAccountUseCaseImplTest {

  private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();
  private static final Iban VALID_IBAN = Iban.of("ES3801283316232166447417");

  private static final AccountHolderName ACCOUNT_HOLDER_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber PASSPORT_NUMBER = AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth();
  private static final Email EMAIL = AccountHolderFixtures.JEFFERSON.getEmail();

  private static final ZonedDateTime CREATED_AT = ZonedDateTime.now(ClockTestFactory.FIXED_CLOCK);

  private static final Clock USE_CASE_CLOCK =
    Clock.fixed(CREATED_AT.toInstant().plusSeconds(7200), CREATED_AT.getZone());

  private static final ZonedDateTime ACTIVATED_AT = ZonedDateTime.now(USE_CASE_CLOCK);


  @Mock
  private LookupBankAccountRepository lookupBankAccountRepository;

  @Mock
  private UpdateBankAccountRepository updateBankAccountRepository;

  @Mock
  private BankAccountActivatedEventPublisher domainEventPublisher;

  @Captor
  private ArgumentCaptor<DomainEvent> eventArgumentCaptor;

  private ActivateBankAccountUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new ActivateBankAccountUseCaseImpl(
      lookupBankAccountRepository,
      updateBankAccountRepository,
      domainEventPublisher,
      USE_CASE_CLOCK
    );
  }

  @Test
  void shouldActivateBankAccount_whenCommandIsValid() {
    BankAccount bankAccount = BankAccount.open(
      ACCOUNT_HOLDER_NAME,
      PASSPORT_NUMBER,
      DATE_OF_BIRTH,
      EMAIL,
      AccountType.CHECKING,
      Currency.USD,
      VALID_IBAN,
      CREATED_AT);

    bankAccount.pullDomainEvents();

    when(lookupBankAccountRepository.byId(BANK_ACCOUNT_ID))
      .thenReturn(Optional.of(bankAccount));

    var command = new ActivateBankAccountCommand(BANK_ACCOUNT_ID);
    useCase.execute(command);

    verify(updateBankAccountRepository).update(bankAccount);
    verify(domainEventPublisher).publish(eventArgumentCaptor.capture());
    verifyNoMoreInteractions(lookupBankAccountRepository, updateBankAccountRepository);

    assertThat(eventArgumentCaptor.getAllValues())
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(BankAccountActivatedEvent.class, event -> {
          assertThat(event.bankAccountId()).isEqualTo(bankAccount.getBankAccountId());
          assertThat(event.occurredAt()).isEqualTo(ACTIVATED_AT);
        }
      );
  }

  @Test
  void shouldThrowAccountRecipientNotFoundException_whenRecipientDoesNotExist() {
    when(lookupBankAccountRepository.byId(BANK_ACCOUNT_ID)).thenReturn(Optional.empty());

    var command = new ActivateBankAccountCommand(BANK_ACCOUNT_ID);

    assertThatThrownBy(() -> useCase.execute(command))
      .isInstanceOf(BankAccountNotFoundException.class);

    verify(lookupBankAccountRepository).byId(BANK_ACCOUNT_ID);
    verifyNoInteractions(updateBankAccountRepository);
    verifyNoMoreInteractions(lookupBankAccountRepository);
  }

  @Test
  void shouldThrowNullPointerException_whenCommandIsNull() {
    assertThatThrownBy(() -> useCase.execute(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("command must not be null");

    verifyNoInteractions(lookupBankAccountRepository, updateBankAccountRepository);
  }
}