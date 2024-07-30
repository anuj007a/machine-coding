package com.graviton.repository.impl;

import com.graviton.enums.TransactionType;
import com.graviton.exception.GravitonException;
import com.graviton.model.Transaction;
import com.graviton.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepositoryImpl implements TransactionRepository {
    // In-memory storage for transactions mapped by customer ID
    private final Map<String, List<Transaction>> transactionMap = new HashMap<>();

    /**
     * Adds a transaction to the repository for a given customer.
     * If the customer does not exist in the repository, a new entry is created.
     *
     * @param customerId The unique identifier for the customer.
     * @param transaction The transaction to be added.
     * @throws IllegalArgumentException if customerId or transaction is null.
     */
    @Override
    public void addTransaction(String customerId, Transaction transaction) {
        if (customerId == null || transaction == null) {
            throw new IllegalArgumentException("Customer ID and transaction cannot be null");
        }
        transactionMap
                .computeIfAbsent(customerId, k -> new ArrayList<>())
                .add(transaction);
    }

    /**
     * Retrieves all transactions for a given customer.
     *
     * @param customerId The unique identifier for the customer.
     * @return A list of transactions for the customer.
     * @throws GravitonException.CustomerNotFoundException if the customer ID does not exist.
     */
    @Override
    public List<Transaction> getTransactions(String customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        List<Transaction> transactions = transactionMap.get(customerId);
        if (transactions == null) {
            throw new GravitonException.CustomerNotFoundException(customerId);
        }
        return transactions;
    }
}
