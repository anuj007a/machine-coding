package org.wraith.service;

import org.wraith.model.CreditCard;

public interface CreditCardService {
    void onboardUser(String userId, String name, String email, String phoneNumber);
    void spend(String userId, double amount);
    double getBalance(String userId);
    void generateStatement(String userId);
    void payBill(String userId, double amount);
}
