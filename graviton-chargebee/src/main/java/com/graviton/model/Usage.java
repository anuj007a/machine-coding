package com.graviton.model;

/**
 * Represents a usage entry for a customer using a particular service.
 *
 * @param customerId The unique identifier for the customer.
 * @param serviceName The name of the service used by the customer.
 */
public record Usage(String customerId, String serviceName) {

    @Override
    public String customerId() {
        return customerId;
    }

    @Override
    public String serviceName() {
        return serviceName;
    }
}
