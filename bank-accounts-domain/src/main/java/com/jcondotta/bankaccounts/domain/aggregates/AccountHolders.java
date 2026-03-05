package com.jcondotta.bankaccounts.domain.aggregates;

import com.jcondotta.bankaccounts.domain.enums.AccountHolderType;
import com.jcondotta.bankaccounts.domain.exceptions.AccountHolderNotFoundException;
import com.jcondotta.bankaccounts.domain.exceptions.CannotDeactivatePrimaryAccountHolderException;
import com.jcondotta.bankaccounts.domain.exceptions.InvalidBankAccountHoldersConfigurationException;
import com.jcondotta.bankaccounts.domain.exceptions.MaxJointAccountHoldersExceededException;
import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;

import java.util.*;

import static com.jcondotta.domain.validation.DomainPreconditions.required;

public final class AccountHolders {

  static final String ACCOUNT_HOLDERS_MUST_BE_PROVIDED = "Account holders must be provided";

  private static final int MIN_PRIMARY = 1;
  private static final int MAX_PRIMARY = 1;
  private static final int MAX_JOINT = 1;

  private final List<AccountHolder> holders;

  private AccountHolders(List<AccountHolder> holders) {
    required(holders, ACCOUNT_HOLDERS_MUST_BE_PROVIDED);

    this.holders = new ArrayList<>(holders);
    validateConfiguration();
  }

  public static AccountHolders of(List<AccountHolder> holders) {
    return new AccountHolders(holders);
  }

  public static AccountHolders of(AccountHolder... holders) {
    required(holders, ACCOUNT_HOLDERS_MUST_BE_PROVIDED);
    return new AccountHolders(Arrays.stream(holders).toList());
  }

  private void validateConfiguration() {
    validatePrimary();
    validateJoint();
  }

  private void validatePrimary() {
    int primaryCount = (int) holders.stream()
      .filter(AccountHolder::isPrimary)
      .count();

    if (primaryCount < MIN_PRIMARY || primaryCount > MAX_PRIMARY) {
      throw new InvalidBankAccountHoldersConfigurationException(primaryCount, MIN_PRIMARY, MAX_PRIMARY);
    }
  }

  private void validateJoint() {
    int jointCount = jointCount();

    if (jointCount > MAX_JOINT) {
      throw new MaxJointAccountHoldersExceededException(jointCount);
    }
  }

  public List<AccountHolder> active() {
    return holders.stream()
      .filter(AccountHolder::isActive)
      .sorted(Comparator.comparing(
        holder -> holder.getAccountHolderType() == AccountHolderType.PRIMARY ? 0 : 1
      ))
      .toList();
  }

  public AccountHolder primary() {
    return holders.stream()
      .filter(AccountHolder::isPrimary)
      .findFirst()
      .orElseThrow();
  }

  public List<AccountHolder> joint() {
    return holders.stream()
      .filter(AccountHolder::isJoint)
      .filter(AccountHolder::isActive)
      .toList();
  }

  public int jointCount() {
    return joint().size();
  }

  public void add(AccountHolder holder) {
    required(holder, "Account holder must not be null");

    if (holder.isPrimary()) {
      throw new IllegalStateException("Primary account holder already exists");
    }

    if (jointCount() >= MAX_JOINT) {
      throw new MaxJointAccountHoldersExceededException(jointCount());
    }

    holders.add(holder);
  }

  public void deactivate(AccountHolderId accountHolderId) {
    var holder = find(accountHolderId)
      .orElseThrow(() -> new AccountHolderNotFoundException(accountHolderId));

    if (holder.isPrimary()) {
      throw new CannotDeactivatePrimaryAccountHolderException();
    }

    holder.deactivate();
  }

  public Optional<AccountHolder> find(AccountHolderId accountHolderId) {
    return holders.stream()
      .filter(holder -> holder.getId().equals(accountHolderId))
      .findFirst();
  }

//  private void validateConfiguration() {
//    validatePrimary();
//    validateJoint();
//  }
//
//  private void validatePrimary() {
//    int primaryCount = (int) holders.stream()
//      .filter(AccountHolder::isPrimary)
//      .count();
//
//    if (primaryCount < MIN_PRIMARY || primaryCount > MAX_PRIMARY) {
//      throw new InvalidBankAccountHoldersConfigurationException(
//        primaryCount,
//        MIN_PRIMARY,
//        MAX_PRIMARY
//      );
//    }
//  }
//
//  private void validateJoint() {
//    int jointCount = jointCount();
//
//    if (jointCount > MAX_JOINT) {
//      throw new MaxJointAccountHoldersExceededException(jointCount);
//    }
//  }
//
//  public List<AccountHolder> all() {
//    return List.copyOf(holders);
//  }
}