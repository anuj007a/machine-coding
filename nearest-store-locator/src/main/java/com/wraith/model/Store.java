package com.wraith.model;

public class Store {
    private final String storeId;
    private final String storeName;
    private final Location location;
    public Store(String storeId, String storeName, Location location) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.location = location;
    }

    public String getStoreId() {
        return storeId;
    }
    public String getStoreName() {
        return storeName;
    }
    public Location getLocation() {
        return location;
    }
}
