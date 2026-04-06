package org.wraith.model;

import java.util.ArrayList;
import java.util.List;

public class CreditCard {
    private final String cardId;
    private double balance;
    private double spent;
    private double creditLimit;
    private final List<Transaction> transactions;
    private double remainingBalance;

    public CreditCard(String cardId) {
        this.cardId = cardId;
        this.balance = 10000.0;
        this.spent = 0.0;
        this.transactions = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }
    public double getCreditLimit() {
        return creditLimit;
    }
    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }
    public double getBalance(double balance) {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public double getSpent() {
        return spent;
    }
}
