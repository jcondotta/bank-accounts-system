package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder;

import com.jcondotta.bankaccounts.application.usecase.addjointaccountholder.AddJointAccountHolderUseCase;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.mapper.AddJointAccountHolderRequestControllerMapper;
import com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.addjointaccountholder.model.AddJointAccountHolderRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@AllArgsConstructor
public class AddJointAccountHolderControllerImpl implements AddJointAccountHolderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddJointAccountHolderControllerImpl.class);

    private final AddJointAccountHolderUseCase addJointAccountHolderUseCase;
    private final AddJointAccountHolderRequestControllerMapper requestMapper;

    @Override
    public ResponseEntity<Void> createJointAccountHolder(UUID bankAccountId, AddJointAccountHolderRequest restRequest) {
        LOGGER.atInfo()
                .setMessage("Received request to create a joint account holder for Bank Account ID")
                .addKeyValue("bankAccountId", bankAccountId)
                .log();

        addJointAccountHolderUseCase.execute(requestMapper.toCommand(bankAccountId, restRequest));
        return ResponseEntity.ok().build();
    }
}