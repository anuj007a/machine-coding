package com.anuj.ratelimiter.service;


import com.anuj.ratelimiter.model.ThrottleRule;

import java.util.HashMap;

public class ThrotleRulesService {
    HashMap<String, ThrottleRule> clientThrotleRules;
    private static volatile ThrotleRulesService instance;

    public ThrotleRulesService(){
        clientThrotleRules = new HashMap<>();
    }

    public void createRule(String clientId, ThrottleRule throtleRule){
        clientThrotleRules.put(clientId, throtleRule);
    }

    public ThrottleRule getClientRules(String clientId){
        return clientThrotleRules.get(clientId);
    }

    public static ThrotleRulesService getInstance(){

        if (instance == null) {
            synchronized (ThrotleRulesService.class) {
                // Double check
                if (instance == null) {
                    instance = new ThrotleRulesService();
                }
            }
        }
        return instance;
    }
}