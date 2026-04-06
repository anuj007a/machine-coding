package com.wraith.service.impl;

import com.wraith.model.Location;
import com.wraith.model.Store;
import com.wraith.repository.StoreRepository;
import com.wraith.service.StoreService;
import com.wraith.strategy.DistanceStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class StoreServiceImpl implements StoreService {

    private final StoreRepository repository;
    private final DistanceStrategy strategy;

    public StoreServiceImpl(StoreRepository repository, DistanceStrategy strategy) {
        this.repository = repository;
        this.strategy = strategy;
    }
    @Override
    public void addStore(Store store) {
        // Implementation to add store to the data structure
        repository.add(store);
    }

    @Override
    public Store findNearest(Location location) {
        // Implementation to find the nearest store using the distance strategy
        double min = Double.MAX_VALUE;
        Store result = null;

        for (Store store : repository.getAll()) {
            double dist = strategy.calculate(location, store.getLocation());
            if (dist < min) {
                min = dist;
                result = store;
            }
        }

        return result;
    }

    @Override
    public List<Store> findKNearest(Location location, int k) {
        // Implementation to find the k nearest stores using the distance strategy
        PriorityQueue<StoreDistance> maxHeap =
                new PriorityQueue<>((a, b) -> Double.compare(b.distance, a.distance));

        for (Store store : repository.getAll()) {
            double dist = strategy.calculate(location, store.getLocation());

            maxHeap.offer(new StoreDistance(store, dist));

            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }

        List<Store> result = new ArrayList<>();
        while (!maxHeap.isEmpty()) {
            result.add(maxHeap.poll().store);
        }

        return result;
    }

    private static class StoreDistance {
        Store store;
        double distance;

        StoreDistance(Store store, double distance) {
            this.store = store;
            this.distance = distance;
        }
    }

}
