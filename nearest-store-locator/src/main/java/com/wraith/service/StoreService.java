package com.wraith.service;

import com.wraith.model.Location;
import com.wraith.model.Store;

import java.util.List;

public interface StoreService {
    void addStore(Store store);
    Store findNearest(Location location);
    List<Store> findKNearest(Location location, int k);
}
