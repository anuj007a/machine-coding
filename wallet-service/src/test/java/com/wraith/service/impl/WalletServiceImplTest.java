package com.wraith.service.impl;

import com.wraith.dto.CreateWalletRequest;
import com.wraith.dto.WalletDTO;
import com.wraith.enums.WalletStatus;
import com.wraith.exception.WalletException;
import com.wraith.exception.WalletNotFoundException;
import com.wraith.model.User;
import com.wraith.model.Wallet;
import com.wraith.repository.TransactionRepository;
import com.wraith.repository.UserRepository;
import com.wraith.repository.WalletLedgerRepository;
import com.wraith.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletLedgerRepository walletLedgerRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    void createWallet_success() {
        CreateWalletRequest request = CreateWalletRequest.builder()
                .userId("u1")
                .initialBalance(100.0)
                .build();

        when(userRepository.findById("u1")).thenReturn(Optional.of(User.builder().userId("u1").build()));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> {
            Wallet wallet = invocation.getArgument(0);
            wallet.setWalletId("w1");
            return wallet;
        });

        WalletDTO response = walletService.createWallet(request);

        assertEquals("w1", response.getWalletId());
        assertEquals("u1", response.getUserId());
        assertEquals(100.0, response.getBalance());
        assertEquals(WalletStatus.ACTIVE, response.getStatus());
    }

    @Test
    void createWallet_userNotFound_throwsException() {
        CreateWalletRequest request = CreateWalletRequest.builder()
                .userId("missing")
                .initialBalance(10.0)
                .build();

        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        WalletException ex = assertThrows(WalletException.class, () -> walletService.createWallet(request));
        assertEquals("USER_NOT_FOUND", ex.getErrorCode());
    }

    @Test
    void getWalletById_notFound_throwsException() {
        when(walletRepository.findById("missing")).thenReturn(Optional.empty());
        assertThrows(WalletNotFoundException.class, () -> walletService.getWalletById("missing"));
    }

    @Test
    void updateWalletStatus_closeWithBalance_throwsException() {
        Wallet wallet = Wallet.builder()
                .walletId("w1")
                .balance(50.0)
                .status(WalletStatus.ACTIVE)
                .locked(false)
                .build();

        when(walletRepository.findByIdWithLock("w1")).thenReturn(Optional.of(wallet));

        WalletException ex = assertThrows(WalletException.class,
                () -> walletService.updateWalletStatus("w1", WalletStatus.CLOSED));
        assertEquals("INVALID_WALLET_STATE", ex.getErrorCode());
    }

    @Test
    void updateWalletStatus_success() {
        Wallet wallet = Wallet.builder()
                .walletId("w1")
                .balance(0.0)
                .status(WalletStatus.ACTIVE)
                .locked(false)
                .build();

        when(walletRepository.findByIdWithLock("w1")).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WalletDTO response = walletService.updateWalletStatus("w1", WalletStatus.INACTIVE);

        assertEquals(WalletStatus.INACTIVE, response.getStatus());
    }

    @Test
    void hasSufficientBalance_true() {
        Wallet wallet = Wallet.builder().walletId("w1").balance(200.0).build();
        when(walletRepository.findById("w1")).thenReturn(Optional.of(wallet));

        assertTrue(walletService.hasSufficientBalance("w1", 50.0));
    }

    @Test
    void getWalletsByUserId_returnsList() {
        Wallet wallet = Wallet.builder().walletId("w1").userId("u1").balance(10.0).status(WalletStatus.ACTIVE).build();
        when(walletRepository.findByUserId("u1")).thenReturn(List.of(wallet));

        List<WalletDTO> result = walletService.getWalletsByUserId("u1");

        assertEquals(1, result.size());
        assertEquals("w1", result.get(0).getWalletId());
    }
}

