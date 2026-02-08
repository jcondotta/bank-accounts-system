package com.jcondotta.bankaccounts.infrastructure.fixtures;

import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderName;
import com.jcondotta.bankaccounts.domain.value_objects.DateOfBirth;
import com.jcondotta.bankaccounts.domain.value_objects.PassportNumber;

import java.time.LocalDate;
import java.time.Month;

public enum AccountHolderFixtures {

  JEFFERSON("Jefferson Condotta", "FH254787", LocalDate.of(1988, Month.JUNE, 24)),
  VIRGINIO("Virginio Condotta", "BC858683", LocalDate.of(1917, Month.DECEMBER, 11)),
  PATRIZIO("Patrizio Condotta", "AA527570", LocalDate.of(1889, Month.FEBRUARY, 18));

  AccountHolderFixtures(String accountHolderName, String passportNumber, LocalDate dateOfBirth) {
    this.accountHolderName = accountHolderName;
    this.passportNumber = passportNumber;
    this.dateOfBirth = dateOfBirth;
  }

  private final String accountHolderName;
  private final String passportNumber;
  private final LocalDate dateOfBirth;

  public AccountHolderName getAccountHolderName() {
    return AccountHolderName.of(accountHolderName);
  }

  public DateOfBirth getDateOfBirth() {
    return DateOfBirth.of(dateOfBirth);
  }

  public PassportNumber getPassportNumber() {
    return PassportNumber.of(passportNumber);
  }
}
