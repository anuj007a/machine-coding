package com.graviton.service;

import com.graviton.enums.TransactionType;
import com.graviton.exception.GravitonException;
import com.graviton.model.Customer;
import com.graviton.model.PackageDetails;
import com.graviton.model.Service;
import com.graviton.model.Transaction;
import com.graviton.repository.CustomerRepository;
import com.graviton.repository.PricingRepository;
import com.graviton.repository.TransactionRepository;
import com.graviton.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PricingRepository pricingRepository;

    @Mock
    private Customer customer;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPurchasePackage_Success() {
        Customer customer = new Customer("C1");
        PackageDetails packageDetails = new PackageDetails("Basic", 100,100.00);

        when(customerRepository.getCustomer("C1")).thenReturn(customer);
        when(pricingRepository.getPackage("Basic")).thenReturn(packageDetails);

        transactionService.purchasePackage("C1", "Basic");

        verify(customerRepository).getCustomer("C1");
        verify(pricingRepository).getPackage("Basic");
        verify(transactionRepository).addTransaction(eq("C1"), any(Transaction.class));

        assertEquals(100, customer.getCreditBalance());
    }

    @Test
    public void testPurchasePackage_CustomerNotFound() {
        when(customerRepository.getCustomer("C1")).thenReturn(null);

        GravitonException.CustomerNotFoundException thrown = assertThrows(
            GravitonException.CustomerNotFoundException.class,
            () -> transactionService.purchasePackage("C1", "Basic")
        );

        assertEquals("Customer not found: C1", thrown.getMessage());
    }

    @Test
    public void testPurchasePackage_PackageNotFound() {
        Customer customer = new Customer("C1");
        when(customerRepository.getCustomer("C1")).thenReturn(customer);
        when(pricingRepository.getPackage("Basic")).thenReturn(null);

        GravitonException.PackageNotFoundException thrown = assertThrows(
            GravitonException.PackageNotFoundException.class,
            () -> transactionService.purchasePackage("C1", "Basic")
        );

        assertEquals("Package not found: Basic", thrown.getMessage());
    }

    @Test
    public void testUseService_Success() {
        Customer customer = new Customer("C1");
        Service service = new Service("S1", 1.0);
        PackageDetails packageDetails = new PackageDetails("Basic", 100, 100.00);

        // Set up mocks
        when(customerRepository.getCustomer("C1")).thenReturn(customer);
        when(pricingRepository.getService("S1")).thenReturn(service);
        when(pricingRepository.getPackage("Basic")).thenReturn(packageDetails);

        // Perform actions
        transactionService.purchasePackage("C1", "Basic");  // This should add credits
        transactionService.useService("C1", "S1");          // This should use credits

        // Verify interactions
        verify(customerRepository, times(2)).getCustomer("C1"); // Adjust to the actual number of calls
        verify(pricingRepository).getService("S1");
        verify(transactionRepository, times(2)).addTransaction(eq("C1"), any(Transaction.class)); // Verify addTransaction was called twice

                // Assert the result
        assertEquals(99.0, customer.getCreditBalance(), 0.001); // Ensure credits are correctly updated
    }




    @Test
    public void testUseService_CustomerNotFound() {
        when(customerRepository.getCustomer("C1")).thenReturn(null);

        GravitonException.CustomerNotFoundException thrown = assertThrows(
            GravitonException.CustomerNotFoundException.class,
            () -> transactionService.useService("C1", "S1")
        );

        assertEquals("Customer not found: C1", thrown.getMessage());
    }

    @Test
    public void testUseService_ServiceNotFound() {
        Customer customer = new Customer("C1");
        when(customerRepository.getCustomer("C1")).thenReturn(customer);
        when(pricingRepository.getService("S1")).thenThrow(new IllegalArgumentException("Service not found"));

        GravitonException.ServiceNotFoundException thrown = assertThrows(
            GravitonException.ServiceNotFoundException.class,
            () -> transactionService.useService("C1", "S1")
        );
        assertEquals("Service not found: S1", thrown.getMessage());
    }

    @Test
    public void testUseService_InsufficientCredits() {
        Service service = new Service("S1", 50);

        // Set up the mock behavior
        when(customerRepository.getCustomer("C1")).thenReturn(customer);
        when(pricingRepository.getService("S1")).thenReturn(service);

        // Simulate insufficient credits scenario
        when(customer.useCredits(50)).thenThrow(new GravitonException.InsufficientCreditsException(50, 0));

        // Assert that the exception is thrown and check the message
        GravitonException.InsufficientCreditsException thrown = assertThrows(
                GravitonException.InsufficientCreditsException.class,
                () -> transactionService.useService("C1", "S1")
        );

        assertEquals("Insufficient credits: Required 50.0, but available is 0.0", thrown.getMessage());
        verify(transactionRepository).addTransaction(eq("C1"), any(Transaction.class));
    }

    @Test
    public void testGetCustomerTransactionsHistory_Success() {
        Customer customer = new Customer("C1");
        Transaction transaction = new Transaction(TransactionType.PURCHASE, 100, 100.00);

        when(customerRepository.getCustomer("C1")).thenReturn(customer);
        when(transactionRepository.getTransactions("C1")).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getCustomerTransactionsHistory("C1");

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(TransactionType.PURCHASE, transactions.get(0).getType());
    }

    @Test
    public void testGetCustomerTransactionsHistory_CustomerNotFound() {
        when(customerRepository.getCustomer("C1")).thenReturn(null);

        GravitonException.CustomerNotFoundException thrown = assertThrows(
            GravitonException.CustomerNotFoundException.class,
            () -> transactionService.getCustomerTransactionsHistory("C1")
        );

        assertEquals("Customer not found: C1", thrown.getMessage());
    }
}
