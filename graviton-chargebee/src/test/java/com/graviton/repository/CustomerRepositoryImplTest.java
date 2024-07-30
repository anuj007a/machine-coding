package com.graviton.repository;

import com.graviton.model.Customer;
import com.graviton.repository.impl.CustomerRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryImplTest {

    private CustomerRepositoryImpl customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepositoryImpl();
    }

    @Test
    void testAddCustomer_Success() {
        Customer customer = new Customer("C1");
        customer.setCreditBalance(100.0);

        assertDoesNotThrow(() -> customerRepository.addCustomer(customer));
    }

    @Test
    void testAddCustomer_NullCustomer() {
        assertThrows(IllegalArgumentException.class, () -> customerRepository.addCustomer(null));
    }

    @Test
    void testGetCustomer_Success() {
        Customer customer = new Customer("C1");
        customer.setCreditBalance(100.0);
        customerRepository.addCustomer(customer);

        Customer retrievedCustomer = customerRepository.getCustomer("C1");
        assertNotNull(retrievedCustomer);
        assertEquals("C1", retrievedCustomer.getId());
    }

    @Test
    void testGetCustomer_CustomerNotFound() {
        assertThrows(IllegalArgumentException.class, () -> customerRepository.getCustomer("NonExistentId"));
    }

    @Test
    void testGetCustomer_NullCustomerId() {
        assertThrows(IllegalArgumentException.class, () -> customerRepository.getCustomer(null));
    }

    @Test
    void testGetCustomerBalance_Success() {
        Customer customer = new Customer("C1");
        customer.setCreditBalance(100.0);
        customerRepository.addCustomer(customer);

        double balance = customerRepository.getCustomerBalance("C1");
        assertEquals(100.0, balance);
    }

    @Test
    void testGetCustomerBalance_CustomerNotFound() {
        assertThrows(IllegalArgumentException.class, () -> customerRepository.getCustomerBalance("NonExistentId"));
    }

    @Test
    void testGetCustomerBalance_NullCustomerId() {
        assertThrows(IllegalArgumentException.class, () -> customerRepository.getCustomerBalance(null));
    }

    @Test
    void testGetAllCustomer_Success() {
        Customer customer1 = new Customer("C1");
        customer1.setCreditBalance(100.0);
        Customer customer2 = new Customer("C2");
        customer2.setCreditBalance(200.0);

        customerRepository.addCustomer(customer1);
        customerRepository.addCustomer(customer2);

        List<Customer> customers = customerRepository.getAllCustomer();
        assertNotNull(customers);
        assertEquals(2, customers.size());
    }
}
