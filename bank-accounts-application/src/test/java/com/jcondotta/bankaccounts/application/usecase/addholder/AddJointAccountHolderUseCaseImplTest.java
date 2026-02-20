package com.jcondotta.bankaccounts.application.usecase.addholder;

import com.jcondotta.bankaccounts.application.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.application.ports.output.messaging.JointAccountHolderAddedEventPublisher;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.LookupBankAccountRepository;
import com.jcondotta.bankaccounts.application.ports.output.persistence.repository.UpdateBankAccountRepository;
import com.jcondotta.bankaccounts.application.usecase.addholder.model.AddJointAccountHolderCommand;
import com.jcondotta.bankaccounts.domain.entities.AccountHolder;
import com.jcondotta.bankaccounts.domain.entities.BankAccount;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.events.DomainEvent;
import com.jcondotta.bankaccounts.domain.events.JointAccountHolderAddedEvent;
import com.jcondotta.bankaccounts.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bankaccounts.domain.value_objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddJointAccountHolderUseCaseImplTest {

  private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();
  private static final Iban VALID_IBAN = Iban.of("ES3801283316232166447417");

  private static final AccountHolderName PRIMARY_ACCOUNT_HOLDER_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName();
  private static final PassportNumber PRIMARY_PASSPORT_NUMBER = AccountHolderFixtures.JEFFERSON.getPassportNumber();
  private static final DateOfBirth PRIMARY_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth();
  private static final Email PRIMARY_EMAIL = AccountHolderFixtures.JEFFERSON.getEmail();

  private static final AccountHolderName JOINT_ACCOUNT_HOLDER_NAME = AccountHolderFixtures.PATRIZIO.getAccountHolderName();
  private static final PassportNumber JOINT_PASSPORT_NUMBER = AccountHolderFixtures.PATRIZIO.getPassportNumber();
  private static final DateOfBirth JOINT_DATE_OF_BIRTH = AccountHolderFixtures.PATRIZIO.getDateOfBirth();
  private static final Email JOINT_EMAIL = AccountHolderFixtures.PATRIZIO.getEmail();

  @Mock
  private LookupBankAccountRepository lookupBankAccountRepository;

  @Mock
  private UpdateBankAccountRepository updateBankAccountRepository;

  @Mock
  private JointAccountHolderAddedEventPublisher domainEventPublisher;

  @Captor
  private ArgumentCaptor<DomainEvent> eventArgumentCaptor;

  private AddJointAccountHolderUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new AddJointAccountHolderUseCaseImpl(
      lookupBankAccountRepository,
      updateBankAccountRepository,
      domainEventPublisher
    );
  }

  @Test
  void shouldAddJointAccountHolder_whenCommandIsValid() {
    BankAccount bankAccount =
      BankAccount.open(
        PRIMARY_ACCOUNT_HOLDER_NAME,
        PRIMARY_PASSPORT_NUMBER,
        PRIMARY_DATE_OF_BIRTH,
        PRIMARY_EMAIL,
        AccountType.CHECKING,
        Currency.EUR,
        VALID_IBAN
      );

    bankAccount.activate();
    bankAccount.pullDomainEvents();

    when(lookupBankAccountRepository.byId(BANK_ACCOUNT_ID))
      .thenReturn(Optional.of(bankAccount));

    var command = new AddJointAccountHolderCommand(BANK_ACCOUNT_ID, JOINT_ACCOUNT_HOLDER_NAME, JOINT_PASSPORT_NUMBER, JOINT_DATE_OF_BIRTH, JOINT_EMAIL);

    useCase.execute(command);
    verify(lookupBankAccountRepository).byId(BANK_ACCOUNT_ID);
    verify(updateBankAccountRepository).update(bankAccount);
    verify(domainEventPublisher).publish(eventArgumentCaptor.capture());
    verifyNoMoreInteractions(lookupBankAccountRepository, updateBankAccountRepository);

    AccountHolder jointAccountHolder = bankAccount.jointAccountHolders().getFirst();

    assertThat(eventArgumentCaptor.getAllValues())
      .hasSize(1)
      .singleElement()
      .isInstanceOfSatisfying(JointAccountHolderAddedEvent.class, event -> {
          assertThat(event.eventId()).isNotNull();
          assertThat(event.bankAccountId()).isEqualTo(bankAccount.id());
          assertThat(event.accountHolderId()).isEqualTo(jointAccountHolder.id());
          assertThat(event.occurredAt()).isNotNull();
        }
      );
  }

  @Test
  void shouldThrowBankAccountNotFoundException_whenBankAccountDoesNotExist() {
    when(lookupBankAccountRepository.byId(BANK_ACCOUNT_ID))
      .thenReturn(Optional.empty());

    var command = new AddJointAccountHolderCommand(BANK_ACCOUNT_ID, JOINT_ACCOUNT_HOLDER_NAME, JOINT_PASSPORT_NUMBER, JOINT_DATE_OF_BIRTH, JOINT_EMAIL);

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