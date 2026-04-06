package org.wraith.model;

import org.wraith.enums.EntryType;

public class LedgerEntry {
    private final String entryId;
    private final String accountId;
    private final double amount;
    private final EntryType type;
    private final String counterparty;
    private final String transactionId;

    public LedgerEntry(String entryId, String accountId, double amount, EntryType type, String counterparty, String transactionId) {
        this.entryId = entryId;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.counterparty = counterparty;
        this.transactionId = transactionId;
    }

    public String getEntryId() {
        return entryId;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public EntryType getType() {
        return type;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
