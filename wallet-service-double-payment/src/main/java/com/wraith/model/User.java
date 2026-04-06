package com.wraith.model;

public class User {
    private final String userId;
    private final String name;
    private final String accountId;

    public User(String userId, String name, String accountId) {
        this.userId = userId;
        this.name = name;
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAccountId() {
        return accountId;
    }
}
