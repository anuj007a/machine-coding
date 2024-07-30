package com.graviton.repository;

import com.graviton.enums.TransactionType;
import com.graviton.exception.GravitonException;
import com.graviton.model.Transaction;
import com.graviton.repository.impl.TransactionRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryImplTest {

    private TransactionRepositoryImpl transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository = new TransactionRepositoryImpl();
    }

    @Test
    void testAddTransaction_Success() {
        Transaction transaction = new Transaction(TransactionType.PURCHASE, 100, 100.00);

        assertDoesNotThrow(() -> transactionRepository.addTransaction("C1", transaction));
    }

    @Test
    void testAddTransaction_NullCustomerId() {
        Transaction transaction = new Transaction(TransactionType.PURCHASE, 100, 100.00);

        assertThrows(IllegalArgumentException.class, () -> transactionRepository.addTransaction(null, transaction));
    }

    @Test
    void testAddTransaction_NullTransaction() {
        assertThrows(IllegalArgumentException.class, () -> transactionRepository.addTransaction("C1", null));
    }

    @Test
    void testGetTransactions_Success() {
        Transaction transaction1 = new Transaction(TransactionType.PURCHASE, 100, 100.00);
        Transaction transaction2 = new Transaction(TransactionType.USAGE, 50, 50.00);
        transactionRepository.addTransaction("C1", transaction1);
        transactionRepository.addTransaction("C1", transaction2);

        List<Transaction> transactions = transactionRepository.getTransactions("C1");

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertEquals(TransactionType.PURCHASE, transactions.get(0).getType());
        assertEquals(TransactionType.USAGE, transactions.get(1).getType());
    }

    @Test
    void testGetTransactions_CustomerNotFound() {
        assertThrows(GravitonException.CustomerNotFoundException.class, () -> transactionRepository.getTransactions("NonExistentCustomer"));
    }

    @Test
    void testGetTransactions_NullCustomerId() {
        assertThrows(IllegalArgumentException.class, () -> transactionRepository.getTransactions(null));
    }
}
