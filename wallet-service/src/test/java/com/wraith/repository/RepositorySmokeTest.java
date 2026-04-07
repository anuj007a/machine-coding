package com.wraith.repository;

import com.wraith.enums.TransactionStatus;
import com.wraith.enums.TransactionType;
import com.wraith.enums.WalletStatus;
import com.wraith.model.Transaction;
import com.wraith.model.User;
import com.wraith.model.Wallet;
import com.wraith.model.WalletLedger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositorySmokeTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WalletLedgerRepository walletLedgerRepository;

    @Test
    void repositoriesPersistAndQuery() {
        User user = userRepository.save(User.builder().email("u1@test.com").name("u1").build());

        Wallet wallet = walletRepository.save(
                Wallet.builder().userId(user.getUserId()).balance(100.0).status(WalletStatus.ACTIVE).locked(false).build()
        );

        Transaction transaction = transactionRepository.save(
                Transaction.builder()
                        .fromWalletId(wallet.getWalletId())
                        .toWalletId(wallet.getWalletId())
                        .amount(5.0)
                        .type(TransactionType.CREDIT)
                        .status(TransactionStatus.SUCCESS)
                        .description("smoke")
                        .build()
        );

        walletLedgerRepository.save(
                WalletLedger.builder()
                        .walletId(wallet.getWalletId())
                        .transactionId(transaction.getTransactionId())
                        .transactionType(TransactionType.CREDIT)
                        .amount(5.0)
                        .balanceBefore(95.0)
                        .balanceAfter(100.0)
                        .build()
        );

        List<Wallet> wallets = walletRepository.findByUserId(user.getUserId());
        assertEquals(1, wallets.size());
        assertEquals(1, transactionRepository.findByWalletId(wallet.getWalletId()).size());
        assertNotNull(walletLedgerRepository.findByTransactionId(transaction.getTransactionId()));
    }
}

