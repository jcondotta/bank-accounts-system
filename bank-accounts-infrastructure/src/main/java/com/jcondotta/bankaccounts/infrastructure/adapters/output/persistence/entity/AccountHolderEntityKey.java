package com.jcondotta.bankaccounts.infrastructure.adapters.output.persistence.entity;

import com.jcondotta.bankaccounts.domain.value_objects.AccountHolderId;
import com.jcondotta.bankaccounts.domain.value_objects.BankAccountId;

public final class AccountHolderEntityKey {

    public static final String ACCOUNT_HOLDER_PK_TEMPLATE = "BANK_ACCOUNT#%s";
    public static final String ACCOUNT_HOLDER_SK_TEMPLATE = "ACCOUNT_HOLDER#%s";

    private AccountHolderEntityKey() {}

    public static String partitionKey(BankAccountId bankAccountId) {
        return ACCOUNT_HOLDER_PK_TEMPLATE.formatted(bankAccountId.value().toString());
    }

    public static String sortKey(AccountHolderId accountHolderId) {
        return ACCOUNT_HOLDER_SK_TEMPLATE.formatted(accountHolderId.value().toString());
    }
}