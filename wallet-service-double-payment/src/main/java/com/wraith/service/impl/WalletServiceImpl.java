package com.wraith.service.impl;

import com.wraith.enums.EntryType;
import com.wraith.model.LedgerEntry;
import com.wraith.model.User;
import com.wraith.repository.LedgerRepository;
import com.wraith.repository.UserRepository;
import com.wraith.service.WalletService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepo;
    private final LedgerRepository ledgerRepo;
    private final Map<String, ReentrantLock> locks = new ConcurrentHashMap<>();
    public WalletServiceImpl(UserRepository userRepo, LedgerRepository ledgerRepo) {
        this.userRepo = userRepo;
        this.ledgerRepo = ledgerRepo;
    }

    @Override
    public List<LedgerEntry> getUserStatements(String userID) {
        User user = userRepo.getUser(userID);
        return ledgerRepo.getLedgerEntries(user.getName());
    }

    @Override
    public void deposit(String userID, Double amount) {
        User user = userRepo.getUser(userID);
        performDoubleEntry("EXTERNAL", user.getName(), amount);
    }

    @Override
    public void withdraw(String userID, Double amount) {
            validateAmount(amount);
            User user = userRepo.getUser(userID);
            performDoubleEntry(user.getName(), "EXTERNAL", amount);
    }

    @Override
    public void transfer(String fromUserID, String toUserID, Double amount) {
            validateAmount(amount);
            User u1 = userRepo.getUser(fromUserID);
            User u2 = userRepo.getUser(toUserID);
            lockBoth(fromUserID, toUserID);
            try {
                if( getBalance(fromUserID) < amount) {
                    throw new IllegalArgumentException("Insufficient balance");
                }
                 performDoubleEntry(u1.getName(), u2.getName(), amount);
            }finally {
                unlockBoth(fromUserID, toUserID);
            }
    }

    @Override
    public double getBalance(String userID) {
        User user = userRepo.getUser(userID);
        return ledgerRepo.getLedgerEntries(user.getName()).stream()
                .mapToDouble(e -> e.getType() == EntryType.CREDIT ? e.getAmount() : -e.getAmount())
                .sum();
    }

    private void validateAmount(Double amount) {
        if (amount < 0) { throw new IllegalArgumentException("Amount must be positive"); }
    }


    private void performDoubleEntry(String from, String to, Double amount) {
        String txnId = UUID.randomUUID().toString();
        LedgerEntry debit = new LedgerEntry(UUID.randomUUID().toString(), from, amount, EntryType.DEBIT, to, txnId);
        LedgerEntry credit = new LedgerEntry(UUID.randomUUID().toString(), to, amount, EntryType.CREDIT, from, txnId);
        ledgerRepo.addEntry(credit);
        ledgerRepo.addEntry(debit);
    }

    private void lockBoth(String fromUserID, String toUserID) {
        getLock(fromUserID).lock();
        getLock(toUserID).lock();
    }

    private void unlockBoth(String fromUserID, String toUserID) {
        getLock(fromUserID).unlock();
        getLock(toUserID).unlock();
    }


    private ReentrantLock getLock( String id){
        locks.putIfAbsent(id, new ReentrantLock());
        return locks.get(id);
    }

}
