package com.wraith.service;

import com.wraith.model.LedgerEntry;

import java.util.List;

public interface WalletService {

    void deposit(String userID, Double amount);
    void withdraw(String userID, Double amount);
    void transfer(String fromUserID, String toUserID, Double amount);
    double getBalance(String userID);
    List<LedgerEntry> getUserStatements(String userID);


}
