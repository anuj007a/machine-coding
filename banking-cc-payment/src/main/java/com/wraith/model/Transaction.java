package org.wraith.model;


import org.wraith.enums.TransactionType;

public class Transaction {
    private final String id;
    private final double amount;
    private final TransactionType type;

    public Transaction(String id, double amount, TransactionType type) {
        this.id = id;
        this.amount = amount;
        this.type = type;
    }
    public String getId() { return id; }
    public double getAmount() { return amount; }
    public TransactionType getType() { return type; }
}
