package com.graviton.repository.impl;

import com.graviton.model.Customer;
import com.graviton.repository.CustomerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRepositoryImpl implements CustomerRepository {
    // In-memory storage for customers
    private final Map<String, Customer> customers = new HashMap<>();

    @Override
    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        customers.put(customer.getId(), customer);
    }

    @Override
    public Customer getCustomer(String customerId) {
        if (customerId == null || !customers.containsKey(customerId)) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        return customers.get(customerId);
    }

    @Override
    public double getCustomerBalance(String customerId) {
        if (customerId == null || !customers.containsKey(customerId)) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        Customer customer = getCustomer(customerId);
        return customer.getCreditBalance();
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customers.values().stream().toList();
    }
}
