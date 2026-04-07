package com.wraith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main Spring Boot Application for Wallet Service.
 *
 * This application provides wallet and transaction management capabilities.
 * Features include:
 * - Wallet creation and management
 * - Fund transfers between wallets
 * - Transaction history and audit trails
 * - Comprehensive error handling and validation
 *
 * Production Considerations:
 * - Optimistic and pessimistic locking for concurrent transaction safety
 * - Transactional integrity for all financial operations
 * - Audit logging via WalletLedger
 * - Input validation at DTO level
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableTransactionManagement
public class WalletServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletServiceApplication.class, args);
    }
}