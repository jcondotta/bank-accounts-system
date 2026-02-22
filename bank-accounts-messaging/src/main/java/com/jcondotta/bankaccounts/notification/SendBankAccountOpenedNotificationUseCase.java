package com.jcondotta.bankaccounts.notification;

import com.jcondotta.bankaccounts.contracts.open.BankAccountOpenedIntegrationPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendBankAccountOpenedNotificationUseCase {

  private final EmailSender emailSender;

  public void execute(BankAccountOpenedIntegrationPayload message) {

    String subject = "Your bank account has been created";
    String body = String.format(
        "Hello Jefferson,\n\nYour bank account with ID %s has been successfully created.",
      message.bankAccountId()
    );

    emailSender.send(
        "jefferson@gmail.com",
        subject,
        body
    );

    log.info("Notification sent to {}", "jefferson@gmail.com");
  }
}
