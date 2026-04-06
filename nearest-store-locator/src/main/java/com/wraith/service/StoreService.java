package org.wraith.service;

import org.wraith.model.Location;
import org.wraith.model.Store;

import java.util.List;

public interface StoreService {
    void addStore(Store store);
    Store findNearest(Location location);
    List<Store> findKNearest(Location location, int k);
}
