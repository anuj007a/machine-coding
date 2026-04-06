package com.wraith.repository;

import com.wraith.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CreditCardRepository {
    private final Map<String, User>  userStorage = new ConcurrentHashMap<>();

    public void save(User user) {
        userStorage.put(user.getUserId(), user);
    }

    public User findByUserId(String userId) {
        return userStorage.get(userId);
    }
}
