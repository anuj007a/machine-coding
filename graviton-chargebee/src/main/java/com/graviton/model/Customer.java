package com.graviton.model;

import com.graviton.exception.GravitonException;

/**
 * Represents a customer in the Graviton application.
 * Manages the customer's credit balance and provides methods to add and use credits.
 */
public class Customer {

    private final String id;
    private double creditBalance;

    public Customer(String id) {
        this.id = id;
        this.creditBalance = 0.0;
    }

    public void setCreditBalance(double creditBalance) {
        this.creditBalance = creditBalance;
    }

    public String getId() {
        return id;
    }

    public double getCreditBalance() {
        return creditBalance;
    }

    public void addCredits(double credits) {
        if (credits < 0) {
            throw new IllegalArgumentException("Credits to add must be non-negative.");
        }
        creditBalance += credits;
    }

    public boolean useCredits(double credits) {
        if (credits < 0) {
            throw new IllegalArgumentException("Credits to use must be non-negative.");
        }
        if (creditBalance >= credits) {
            creditBalance -= credits;
            return true;
        } else {
            throw new GravitonException.InsufficientCreditsException(credits, getCreditBalance());
        }
    }
}
