package com.graviton.repository;

import com.graviton.model.Customer;

import java.util.List;

public interface CustomerRepository {
    void addCustomer(Customer customer);

    Customer getCustomer(String customerId);

    double getCustomerBalance(String customerId);

    List<Customer> getAllCustomer();
}
