package com.graviton.service;

import com.graviton.model.Customer;
import com.graviton.model.PackageDetails;
import com.graviton.model.Service;

import java.util.List;

public interface PricingService {

    // Customer API
    void addCustomer(Customer customer);

    Customer getCustomer(String customerId);

    double getCustomerBalance(String customerId);

    List<Customer> getAllCustomer();

    // Package and Service API

    PackageDetails getPackage(String packageName);

    void addPackage(PackageDetails packageDetails);

    Service getService(String serviceName);

    void addService(Service service);
}
