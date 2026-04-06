package com.wraith.repository;

import com.wraith.model.Store;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StoreRepository {
    private final Map<String, Store> storeMap = new HashMap<>();

    public void add(Store store) {
        storeMap.put(store.getStoreId(), store);
    }

    public Collection<Store> getAll() {
        return storeMap.values();
    }
}
