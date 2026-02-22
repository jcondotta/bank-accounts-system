package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity;

public enum OutboxStatus {
    PENDING,
    PUBLISHED,
    FAILED
}