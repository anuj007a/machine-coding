package com.graviton.service;

import com.graviton.model.Transaction;

import java.util.List;

public interface TransactionService {
    void useService(String customerId, String serviceName);

    void purchasePackage(String customerId, String packageName);

    List<Transaction> getCustomerTransactionsHistory(String customerId);

}
