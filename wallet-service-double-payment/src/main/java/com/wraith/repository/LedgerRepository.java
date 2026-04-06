package com.wraith.repository;

import com.wraith.model.LedgerEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LedgerRepository {
    private final Map<String, List<LedgerEntry>> ledger = new HashMap<>();


    public void addEntry(LedgerEntry entry) {
        ledger.computeIfAbsent(entry.getAccountId(), k -> new java.util.ArrayList<>()).add(entry);
    }

    public List<LedgerEntry> getLedgerEntries(String accountId) {
        return ledger.getOrDefault(accountId, java.util.Collections.emptyList());
    }
}
