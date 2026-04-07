package com.wraith.service;

import com.wraith.dto.CreateWalletRequest;
import com.wraith.dto.WalletDTO;
import com.wraith.enums.WalletStatus;

import java.util.List;

/**
 * Interface for Wallet Service.
 *
 * Defines all wallet-related business operations.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
public interface WalletService {

    /**
     * Create a new wallet for a user.
     */
    WalletDTO createWallet(CreateWalletRequest request);

    /**
     * Retrieve wallet details by wallet ID.
     */
    WalletDTO getWalletById(String walletId);

    /**
     * Check wallet balance.
     */
    Double getBalance(String walletId);

    /**
     * Get all wallets for a user.
     */
    List<WalletDTO> getWalletsByUserId(String userId);

    /**
     * Update wallet status.
     */
    WalletDTO updateWalletStatus(String walletId, WalletStatus newStatus);

    /**
     * Check if wallet has sufficient balance.
     */
    boolean hasSufficientBalance(String walletId, Double amount);
}

