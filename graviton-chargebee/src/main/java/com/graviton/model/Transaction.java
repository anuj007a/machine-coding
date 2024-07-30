package com.graviton.model;

import com.graviton.enums.TransactionType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a transaction in the system.
 * Contains details about the transaction type, the amount of credits involved, and the resulting balance.
 */
public class Transaction {
    private final String id;
    private final String transactionTime;
    private final TransactionType type;
    private final double credits;
    private final double balance;

    public Transaction(TransactionType type, double credits, double balance) {
        this.id = UUID.randomUUID().toString();  // Generate a unique identifier for the transaction
        this.transactionTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());  // Format the current date and time
        this.type = type;
        this.credits = credits;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public TransactionType getType() {
        return type;
    }

    public double getCredits() {
        return credits;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", transactionTime='" + transactionTime + '\'' +
                ", type=" + type +
                ", credits=" + credits +
                ", balance=" + balance +
                '}';
    }
}
