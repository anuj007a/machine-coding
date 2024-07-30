package com.anuj.ratelimiter.model;

public class ThrottleRule {
    public long bucketSize;
    public long refillRate;

    public ThrottleRule(){
        this.bucketSize = 10;
        this.refillRate = 10;
    }

    public ThrottleRule(long bucketSize, long refillRate) {
        this.bucketSize = bucketSize;
        this.refillRate = refillRate;
    }

    public long getBucketSize() {
        return bucketSize;
    }

    public long getRefillRate() {
        return refillRate;
    }
}