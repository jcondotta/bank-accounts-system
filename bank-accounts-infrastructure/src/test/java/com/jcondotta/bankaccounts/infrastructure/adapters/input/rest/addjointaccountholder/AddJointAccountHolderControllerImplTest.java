package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder;

import com.jcondotta.bankaccounts.application.usecase.addjointaccountholder.AddJointAccountHolderUseCase;
import com.jcondotta.bankaccounts.application.usecase.addjointaccountholder.model.AddJointAccountHolderCommand;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.mapper.AddJointAccountHolderRequestControllerMapper;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.model.AddJointAccountHolderRequest;
import com.jcondotta.bankaccounts.infrastructure.fixtures.AccountHolderFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddJointAccountHolderControllerImplTest {

  private static final UUID BANK_ACCOUNT_UUID = BankAccountId.newId().value();

  private static final String VALID_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName().value();
  private static final String VALID_PASSPORT = AccountHolderFixtures.JEFFERSON.getPassportNumber().value();
  private static final LocalDate VALID_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth().value();

  @Mock
  private AddJointAccountHolderUseCase useCase;

  @Mock
  private AddJointAccountHolderRequestControllerMapper requestMapper;

  @Mock
  private AddJointAccountHolderCommand command;

  @Captor
  ArgumentCaptor<AddJointAccountHolderCommand> commandCaptor;

  private AddJointAccountHolderControllerImpl controller;

  @BeforeEach
  void setUp() {
    controller = new AddJointAccountHolderControllerImpl(useCase, requestMapper);
  }

//  @Test
//  void shouldCreateJointAccountHolderAndReturnOk() {
//    var restRequest = new AddJointAccountHolderRequest(VALID_NAME, VALID_DATE_OF_BIRTH, VALID_PASSPORT);
//
//    when(requestMapper.toCommand(BANK_ACCOUNT_UUID, restRequest))
//      .thenReturn(command);
//
//    ResponseEntity<Void> response = controller.createJointAccountHolder(BANK_ACCOUNT_UUID, restRequest);
//
//    verify(requestMapper).toCommand(BANK_ACCOUNT_UUID, restRequest);
//    verify(useCase).execute(commandCaptor.capture());
//
//    assertThat(commandCaptor.getValue()).isEqualTo(command);
//    assertThat(response.getStatusCode().value()).isEqualTo(200);
//    assertThat(response.getBody()).isNull();
//  }
}