package com.graviton.service;

import com.graviton.model.Customer;
import com.graviton.model.PackageDetails;
import com.graviton.model.Service;
import com.graviton.repository.CustomerRepository;
import com.graviton.repository.PricingRepository;
import com.graviton.service.impl.PricingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PricingServiceImplTest {

    @Mock
    private PricingRepository pricingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private PricingServiceImpl pricingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCustomer() {
        Customer customer = new Customer("C1");
        pricingService.addCustomer(customer);
        verify(customerRepository, times(1)).addCustomer(customer);
    }

    @Test
    public void testGetCustomer() {
        Customer customer = new Customer("C1");
        when(customerRepository.getCustomer("C1")).thenReturn(customer);
        Customer result = pricingService.getCustomer("C1");
        assertNotNull(result);
        assertEquals("C1", result.getId());
    }

    @Test
    public void testGetCustomerBalance() {
        when(customerRepository.getCustomerBalance("C1")).thenReturn(100.0);
        double balance = pricingService.getCustomerBalance("C1");
        assertEquals(100.0, balance);
    }

    @Test
    public void testGetAllCustomer() {
        Customer customer1 = new Customer("C1");
        Customer customer2 = new Customer("C2");
        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);
        when(customerRepository.getAllCustomer()).thenReturn(customers);
        List<Customer> result = pricingService.getAllCustomer();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetPackage() {
        PackageDetails packageDetails = new PackageDetails("Basic", 100, 100.00);
        when(pricingRepository.getPackage("Basic")).thenReturn(packageDetails);
        PackageDetails result = pricingService.getPackage("Basic");
        assertNotNull(result);
        assertEquals("Basic", result.getName());
        assertEquals(100, result.getCredits());
    }

    @Test
    public void testAddPackage() {
        PackageDetails packageDetails = new PackageDetails("Basic", 100,100.00);
        pricingService.addPackage(packageDetails);
        verify(pricingRepository, times(1)).addPackage(packageDetails);
    }

    @Test
    public void testGetService() {
        Service service = new Service("S1", 1);
        when(pricingRepository.getService("S1")).thenReturn(service);
        Service result = pricingService.getService("S1");
        assertNotNull(result);
        assertEquals("S1", result.getServiceName());
        assertEquals(1, result.getCreditCost());
    }

    @Test
    public void testAddService() {
        Service service = new Service("S1", 1);
        pricingService.addService(service);
        verify(pricingRepository, times(1)).addService(service);
    }
}
