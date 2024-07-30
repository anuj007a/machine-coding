package com.graviton.service.impl;

import com.graviton.model.Customer;
import com.graviton.model.PackageDetails;
import com.graviton.model.Service;
import com.graviton.repository.CustomerRepository;
import com.graviton.repository.PricingRepository;
import com.graviton.service.PricingService;

import java.util.List;

/**
 * Implementation of the PricingService interface.
 * Provides methods for managing customers, packages, and services.
 */
public class PricingServiceImpl implements PricingService {
    private final PricingRepository pricingRepository;
    private final CustomerRepository customerRepository;


    public PricingServiceImpl(PricingRepository pricingRepository, CustomerRepository customerRepository) {
        this.pricingRepository = pricingRepository;
        this.customerRepository = customerRepository;
    }


    @Override
    public void addCustomer(Customer customer) {
        customerRepository.addCustomer(customer);
    }


    @Override
    public Customer getCustomer(String customerId) {
        return customerRepository.getCustomer(customerId);
    }


    @Override
    public double getCustomerBalance(String customerId) {
        return customerRepository.getCustomerBalance(customerId);
    }


    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.getAllCustomer();
    }


    @Override
    public PackageDetails getPackage(String packageName) {
        return pricingRepository.getPackage(packageName);
    }


    public void addPackage(PackageDetails packageDetails) {
        pricingRepository.addPackage(packageDetails);
    }


    @Override
    public Service getService(String serviceName) {
        return pricingRepository.getService(serviceName);
    }


    public void addService(Service service) {
        pricingRepository.addService(service);
    }
}
