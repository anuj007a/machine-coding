package com.graviton.model;

/**
 * Represents a service that can be used by customers.
 * Contains the service name and the cost in credits for using the service.
 */
public class Service {
    private String serviceName;
    private double creditCost;

    public Service() {
    }

    public Service(String serviceName, double creditCost) {
        this.serviceName = serviceName;
        this.creditCost = creditCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getCreditCost() {
        return creditCost;
    }

    public void setCreditCost(double creditCost) {
        this.creditCost = creditCost;
    }
}
