package com.jcondotta.bankaccounts.notification;

public interface EmailSender {

  void send(String to, String subject, String body);
}
