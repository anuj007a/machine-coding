package org.wraith.model;

import org.wraith.util.IdGenerator;

public class User {
    private String name;
    private String email;
    private String phoneNumber;
    private CreditCard creditCard;
    private String userId;

    public User(String userId, String name, String email, String phoneNumber, CreditCard creditCard) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.creditCard = creditCard;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

        public String getUserId() {
            return userId;
        }
}
