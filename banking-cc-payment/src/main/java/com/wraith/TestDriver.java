package com.wraith;

import com.wraith.repository.CreditCardRepository;
import com.wraith.service.CreditCardService;
import com.wraith.service.impl.CreditCardServiceImpl;
import com.wraith.strategy.SimpleInterestStrategy;


public class TestDriver {
    public static void main(String[] args) {
        CreditCardRepository repo = new CreditCardRepository();
    CreditCardService service =
            new CreditCardServiceImpl(repo, new SimpleInterestStrategy());

    // Happy Flow
        service.onboardUser("U1", "Anuj", "user_1@gmail.com","1234567890");

        service.spend("U1", 1000);
        service.spend("U1", 500);

        System.out.println("Balance: " + service.getBalance("U1"));

        service.payBill("U1", 1000); // Partial

        System.out.println("Balance after partial payment: " +
                service.getBalance("U1"));

        service.generateStatement("U1");

    // Failure Scenario
        try {
        service.payBill("U1", 10000);
    } catch (Exception e) {
        System.out.println("\nExpected Error: " + e.getMessage());
    }
    }
}