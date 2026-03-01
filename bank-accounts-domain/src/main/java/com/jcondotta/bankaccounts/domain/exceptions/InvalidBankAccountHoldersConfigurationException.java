package com.jcondotta.bankaccounts.domain.exceptions;

public class InvalidBankAccountHoldersConfigurationException extends DomainRuleValidationException {

  public InvalidBankAccountHoldersConfigurationException(int actual, int min, int max) {
    super(
      String.format(
        "BankAccount must have between %d and %d primary account holders. Actual: %d",
        min,
        max,
        actual
      )
    );
  }
}
