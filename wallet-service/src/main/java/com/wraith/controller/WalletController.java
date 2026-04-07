package com.wraith.controller;

import com.wraith.dto.*;
import com.wraith.service.*;
import com.wraith.enums.WalletStatus;
import com.wraith.enums.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Wallet and Transaction Management.
 *
 * Provides endpoints for wallet operations and transaction management.
 * All endpoints are secured and require proper validation.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Wallet Management", description = "APIs for managing wallets and transactions")
public class WalletController {

    private final WalletService walletService;
    private final TransactionService transactionService;

    // ==================== WALLET ENDPOINTS ====================

    /**
     * Create a new wallet for a user.
     */
    @PostMapping
    @Operation(summary = "Create a new wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wallet created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<WalletDTO> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        log.info("Creating wallet for user: {}", request.getUserId());
        WalletDTO wallet = walletService.createWallet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    /**
     * Retrieve wallet details by wallet ID.
     */
    @GetMapping("/{walletId}")
    @Operation(summary = "Get wallet details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wallet retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<WalletDTO> getWallet(@PathVariable String walletId) {
        log.debug("Fetching wallet: {}", walletId);
        WalletDTO wallet = walletService.getWalletById(walletId);
        return ResponseEntity.ok(wallet);
    }

    /**
     * Check wallet balance.
     */
    @GetMapping("/{walletId}/balance")
    @Operation(summary = "Check wallet balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<Double> getBalance(@PathVariable String walletId) {
        log.debug("Fetching balance for wallet: {}", walletId);
        Double balance = walletService.getBalance(walletId);
        return ResponseEntity.ok(balance);
    }

    /**
     * Get all wallets for a user.
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all wallets for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wallets retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<WalletDTO>> getUserWallets(@PathVariable String userId) {
        log.debug("Fetching wallets for user: {}", userId);
        List<WalletDTO> wallets = walletService.getWalletsByUserId(userId);
        return ResponseEntity.ok(wallets);
    }

    /**
     * Update wallet status (deactivate or close).
     */
    @PutMapping("/{walletId}/status")
    @Operation(summary = "Update wallet status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wallet status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status change"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<WalletDTO> updateWalletStatus(@PathVariable String walletId,
                                                         @RequestParam WalletStatus status) {
        log.info("Updating wallet status: {} to {}", walletId, status);
        WalletDTO wallet = walletService.updateWalletStatus(walletId, status);
        return ResponseEntity.ok(wallet);
    }

    // ==================== TRANSACTION ENDPOINTS ====================

    /**
     * Add money to a wallet.
     */
    @PostMapping("/{walletId}/add-money")
    @Operation(summary = "Add money to wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<TransactionDTO> addMoney(@PathVariable String walletId,
                                                   @Valid @RequestBody AddMoneyRequest request) {
        log.info("Adding money to wallet: {}", walletId);
        request.setWalletId(walletId);
        TransactionDTO transaction = transactionService.addMoney(request);
        return ResponseEntity.ok(transaction);
    }

    /**
     * Transfer funds between two wallets.
     */
    @PostMapping("/transfer")
    @Operation(summary = "Transfer funds between wallets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funds transferred successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or insufficient balance"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<TransactionDTO> transferFunds(@Valid @RequestBody TransferFundsRequest request) {
        log.info("Transferring funds from {} to {}", request.getFromWalletId(), request.getToWalletId());
        TransactionDTO transaction = transactionService.transferFunds(request);
        return ResponseEntity.ok(transaction);
    }

    /**
     * Retrieve transaction details by transaction ID.
     */
    @GetMapping("/transactions/{transactionId}")
    @Operation(summary = "Get transaction details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable String transactionId) {
        log.debug("Fetching transaction: {}", transactionId);
        TransactionDTO transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    /**
     * List transactions for a wallet with optional filters.
     */
    @GetMapping("/{walletId}/transactions")
    @Operation(summary = "List transactions for a wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<List<TransactionDTO>> listTransactions(
            @PathVariable String walletId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) TransactionType type) {
        log.debug("Listing transactions for wallet: {}", walletId);
        List<TransactionDTO> transactions = transactionService.listTransactions(walletId, startDate, endDate, type);
        return ResponseEntity.ok(transactions);
    }

    /**
     * List transactions for a wallet with pagination.
     */
    @GetMapping("/{walletId}/transactions/paginated")
    @Operation(summary = "List transactions for a wallet (paginated)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<Page<TransactionDTO>> listTransactionsPaginated(
            @PathVariable String walletId,
            Pageable pageable) {
        log.debug("Listing transactions for wallet (paginated): {}", walletId);
        Page<TransactionDTO> transactions = transactionService.listTransactionsPaginated(walletId, pageable);
        return ResponseEntity.ok(transactions);
    }
}
