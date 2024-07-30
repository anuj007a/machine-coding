package com.graviton.repository.impl;

import com.graviton.model.PackageDetails;
import com.graviton.model.Service;
import com.graviton.repository.PricingRepository;

import java.util.HashMap;
import java.util.Map;

public class PricingRepositoryImpl implements PricingRepository {
    // In-memory storage for packages and services
    private final Map<String, PackageDetails> packages = new HashMap<>();
    private final Map<String, Service> services = new HashMap<>();

    @Override
    public PackageDetails getPackage(String packageName) {
        // Check if packageName is valid
        if (packageName == null) {
            throw new IllegalArgumentException("Package name cannot be null");
        }
        // Retrieve the package details from the map
        PackageDetails packageDetails = packages.get(packageName);
        if (packageDetails == null) {
            throw new IllegalArgumentException("Package not found: " + packageName);
        }
        return packageDetails;
    }


    @Override
    public void addPackage(PackageDetails packageDetails) {
        // Validate input
        if (packageDetails == null || packageDetails.getName() == null) {
            throw new IllegalArgumentException("Package details and package name cannot be null");
        }
        // Add the package to the map
        packages.put(packageDetails.getName(), packageDetails);
    }


    @Override
    public Service getService(String serviceName) {
        // Check if serviceName is valid
        if (serviceName == null) {
            throw new IllegalArgumentException("Service name cannot be null");
        }
        // Retrieve the service from the map
        Service service = services.get(serviceName);
        if (service == null) {
            throw new IllegalArgumentException("Service not found: " + serviceName);
        }
        return service;
    }


    @Override
    public void addService(Service service) {
        // Validate input
        if (service == null || service.getServiceName() == null) {
            throw new IllegalArgumentException("Service details and service name cannot be null");
        }
        // Add the service to the map
        services.put(service.getServiceName(), service);
    }
}
