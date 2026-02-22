package com.jcondotta.bankaccounts.notification;

import com.jcondotta.bankaccounts.contracts.activate.BankAccountActivatedIntegrationPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendBankAccountActivatedNotificationUseCase {

  private final EmailSender emailSender;

  public void execute(BankAccountActivatedIntegrationPayload event) {

    String subject = "Your bank account has been activated";
    String body = String.format(
        "Hello Jefferson,\n\nYour bank account with ID %s has been successfully activated.",
        event.bankAccountId()
    );

    emailSender.send(
        "jefferson@gmail.com",
        subject,
        body
    );

    log.info("Notification sent to {}", "jefferson@gmail.com");
  }
}
