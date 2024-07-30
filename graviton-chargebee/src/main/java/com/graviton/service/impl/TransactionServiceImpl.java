package com.graviton.service.impl;

import com.graviton.enums.TransactionType;
import com.graviton.exception.GravitonException;
import com.graviton.model.Customer;
import com.graviton.model.PackageDetails;
import com.graviton.model.Service;
import com.graviton.model.Transaction;
import com.graviton.repository.PricingRepository;
import com.graviton.repository.CustomerRepository;
import com.graviton.repository.TransactionRepository;
import com.graviton.service.TransactionService;

import java.util.List;

/**
 * Implementation of the TransactionService interface.
 * Manages transactions related to customer purchases and service usage.
 */
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final PricingRepository pricingRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, PricingRepository pricingRepository, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.pricingRepository = pricingRepository;
    }

    @Override
    public void purchasePackage(String customerId, String packageName) {
        Customer customer = customerRepository.getCustomer(customerId);
        if (customer == null) {
            throw new GravitonException.CustomerNotFoundException(customerId);
        }

        PackageDetails packageDetails = pricingRepository.getPackage(packageName);
        if (packageDetails == null) {
            throw new GravitonException.PackageNotFoundException(packageName);
        }

        // Add credits to the customer and record the purchase transaction
        customer.addCredits(packageDetails.getCredits());
        transactionRepository.addTransaction(customerId, new Transaction(TransactionType.PURCHASE, packageDetails.getCredits(), customer.getCreditBalance()));
    }

    @Override
    public void useService(String customerId, String serviceName) {
        // Retrieve the customer and service details
        Customer customer = customerRepository.getCustomer(customerId);
        if (customer == null) {
            throw new GravitonException.CustomerNotFoundException(customerId);
        }

        Service service;
        try {
            service = pricingRepository.getService(serviceName);
        } catch (IllegalArgumentException e) {
            throw new GravitonException.ServiceNotFoundException(serviceName);
        }

        double creditCost = service.getCreditCost();

        // Attempt to use credits and record the transaction
        try {
            if (customer.useCredits(creditCost)) {
                transactionRepository.addTransaction(customerId, new Transaction(TransactionType.USAGE, -creditCost, customer.getCreditBalance()));
            }
        } catch (GravitonException.InsufficientCreditsException e) {
            // Record the denied transaction if credits are insufficient
            transactionRepository.addTransaction(customerId, new Transaction(TransactionType.DENIED, creditCost, customer.getCreditBalance()));
            throw e;
        }
    }

    @Override
    public List<Transaction> getCustomerTransactionsHistory(String customerId) {
        Customer customer = customerRepository.getCustomer(customerId);
        if (customer == null) {
            throw new GravitonException.CustomerNotFoundException(customerId);
        }
        return transactionRepository.getTransactions(customerId);
    }
}
