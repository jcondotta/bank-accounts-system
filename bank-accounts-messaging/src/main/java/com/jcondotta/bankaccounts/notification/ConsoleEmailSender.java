package com.jcondotta.bankaccounts.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsoleEmailSender implements EmailSender {

  @Override
  public void send(String to, String subject, String body) {
    log.info("""
        === SENDING EMAIL ===
        To: {}
        Subject: {}
        Body:
        {}
        =====================
        """, to, subject, body);
  }
}
