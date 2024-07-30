package com.anuj.ratelimiter.service;

import com.anuj.ratelimiter.algorithm.RateLimiter;
import com.anuj.ratelimiter.algorithm.TokenBucket;
import com.anuj.ratelimiter.model.ThrottleRule;

import java.util.HashMap;

public class RateLimiterService {
    HashMap<String, ThrottleRule> clientRulesCache;
    HashMap<String, RateLimiter> rateLimiterHashMap;
    ThrotleRulesService throtleRulesService;

    public RateLimiterService(){
        this.throtleRulesService = ThrotleRulesService.getInstance();
        this.clientRulesCache = new HashMap<>();
        this.rateLimiterHashMap = new HashMap<>();
    }


    public boolean isRateLimitedUserRequest(String userId){
        createUserIfNotTheir(userId);
        return rateLimiterHashMap.get(userId).allowRequest();
    }

    private void createUserIfNotTheir(String userId){
        if(!clientRulesCache.containsKey(userId)){
            ThrottleRule clientRules = throtleRulesService.getClientRules(userId);
            clientRulesCache.put(userId, clientRules);
        }
        if(!rateLimiterHashMap.containsKey(userId)){
            ThrottleRule throtleRule = clientRulesCache.get(userId);
            RateLimiter rateLimiter = new TokenBucket(throtleRule.getBucketSize(), throtleRule.getRefillRate());
            rateLimiterHashMap.put(userId,rateLimiter);
        }
    }
}