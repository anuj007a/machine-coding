package com.graviton.model;

/**
 * Represents a purchase made by a customer.
 * Contains the customer ID and the name of the package purchased.
 */
public record Purchase(String customerId, String packageName) {

    public String customerId() {
        return customerId;
    }

    public String packageName() {
        return packageName;
    }
}
