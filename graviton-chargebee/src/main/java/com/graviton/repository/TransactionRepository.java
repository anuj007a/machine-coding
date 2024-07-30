package com.graviton.repository;

import com.graviton.model.Transaction;

import java.util.List;

public interface TransactionRepository {
    void addTransaction(String customerId, Transaction transaction);

    List<Transaction> getTransactions(String customerId);
}
