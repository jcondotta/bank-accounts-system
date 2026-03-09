package com.jcondotta.banking.recipients.infrastructure.bankaccount.adapters.input.rest.list_recipients;

import com.jcondotta.banking.recipients.application.bankaccount.query.list_recipients.RecipientView;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@RequestMapping("${api.v1.recipients.root-path}")
public interface ListRecipientsController {

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  List<RecipientView> listRecipients(
    @PathVariable("bank-account-id") UUID bankAccountId
  );
}