package com.wraith.service;

public interface CreditCardService {
    void onboardUser(String userId, String name, String email, String phoneNumber);
    void spend(String userId, double amount);
    double getBalance(String userId);
    void generateStatement(String userId);
    void payBill(String userId, double amount);
}
