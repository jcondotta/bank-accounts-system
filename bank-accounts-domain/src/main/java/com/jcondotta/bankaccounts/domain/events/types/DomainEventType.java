package com.jcondotta.bankaccounts.domain.events.types;

public enum DomainEventType {

    BANK_ACCOUNT_OPENED("BankAccountOpened"),
    BANK_ACCOUNT_ACTIVATED("BankAccountActivated"),
    BANK_ACCOUNT_BLOCKED("BankAccountBlocked"),
    JOINT_ACCOUNT_HOLDER_ADDED("JointAccountHolderAdded");

    private final String value;

    DomainEventType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
