package com.jcondotta.bankaccounts.domain.enums;

public enum HolderType {
    PRIMARY, JOINT;

    public boolean isPrimary() {
        return this == PRIMARY;
    }
    public boolean isJoint() {
        return this == JOINT;
    }
}
