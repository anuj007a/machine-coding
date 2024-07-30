package com.graviton.model;

/**
 * Represents a package that a customer can purchase.
 * Contains details about the package's name, the number of credits it provides, and its price.
 */
public class PackageDetails {

    private String name;
    private int credits;
    private double price;

    // Default constructor
    public PackageDetails() {
    }

    public PackageDetails(String name, int credits, double price) {
        this.name = name;
        this.credits = credits;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Package name cannot be null or empty.");
        }
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        if (credits <= 0) {
            throw new IllegalArgumentException("Number of credits must be positive.");
        }
        this.credits = credits;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        this.price = price;
    }
}
