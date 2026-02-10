package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount;

import com.jcondotta.bankaccounts.application.usecase.openbankaccount.OpenBankAccountUseCase;
import com.jcondotta.bankaccounts.application.usecase.openbankaccount.model.OpenBankAccountCommand;
import com.jcondotta.bankaccounts.application.usecase.openbankaccount.model.OpenBankAccountResult;
import com.jcondotta.bankaccounts.domain.enums.AccountType;
import com.jcondotta.bankaccounts.domain.enums.Currency;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.mapper.OpenBankAccountRequestControllerMapper;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.OpenBankAccountRequest;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.openbankaccount.model.PrimaryAccountHolderRequest;
import com.jcondotta.bankaccounts.infrastructure.arguments_provider.AccountTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bankaccounts.infrastructure.fixtures.AccountHolderFixtures;
import com.jcondotta.bankaccounts.infrastructure.properties.BankAccountsURIProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenBankAccountControllerImplTest {

    private static final UUID BANK_ACCOUNT_UUID = UUID.randomUUID();

    private static final String VALID_NAME = AccountHolderFixtures.JEFFERSON.getAccountHolderName().value();
    private static final String VALID_PASSPORT = AccountHolderFixtures.JEFFERSON.getPassportNumber().value();
    private static final LocalDate VALID_DATE_OF_BIRTH = AccountHolderFixtures.JEFFERSON.getDateOfBirth().value();

    private static final URI EXPECTED_LOCATION_URI =
            URI.create("https://api.jcondotta.com/v1/bank-accounts/" + BANK_ACCOUNT_UUID);

    @Mock
    private OpenBankAccountUseCase useCase;

    private OpenBankAccountRequestControllerMapper requestMapper = Mappers.getMapper(OpenBankAccountRequestControllerMapper.class);

    @Mock
    private BankAccountsURIProperties uriProperties;

    private OpenBankAccountControllerImpl controller;

    @BeforeEach
    void setUp() {
        controller =
                new OpenBankAccountControllerImpl(useCase, requestMapper, uriProperties);
    }

//    @ParameterizedTest
//    @ArgumentsSource(AccountTypeAndCurrencyArgumentsProvider.class)
//    void shouldCreateAccountRecipientAndReturnCreatedResponse_whenRequestIsValid(AccountType accountType, Currency currency) {
//        var primaryAccountHolderRequest = new PrimaryAccountHolderRequest(VALID_NAME, VALID_PASSPORT, VALID_DATE_OF_BIRTH);
//        var request = new OpenBankAccountRequest(
//                accountType,
//                currency,
//                primaryAccountHolderRequest
//        );
//
//        when(requestMapper.toCommand(request)).thenReturn(openBankAccountCommand);
//
//        when(useCase.execute(openBankAccountCommand)).thenReturn(new OpenBankAccountResult(BankAccountId.of(BANK_ACCOUNT_UUID), ZonedDateTime.now()));
//        when(uriProperties.bankAccountURI(BANK_ACCOUNT_UUID)).thenReturn(EXPECTED_LOCATION_URI);
//
//        ResponseEntity<Void> response = controller.openBankAccount(request);
//
//        assertThat(response.getStatusCode().value()).isEqualTo(201);
//        assertThat(response.getHeaders().getLocation()).isEqualTo(EXPECTED_LOCATION_URI);
//        assertThat(response.getBody()).isNull();
//
//        verify(requestMapper).toCommand(request);
//        verify(useCase).execute(openBankAccountCommand);
//        verify(uriProperties).bankAccountURI(BANK_ACCOUNT_UUID);
//
//        verifyNoMoreInteractions(requestMapper, useCase, uriProperties);
//    }

}