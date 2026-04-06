package org.wraith.service.impl;

import org.wraith.enums.TransactionType;
import org.wraith.model.CreditCard;
import org.wraith.model.Transaction;
import org.wraith.model.User;
import org.wraith.repository.CreditCardRepository;
import org.wraith.service.CreditCardService;
import org.wraith.strategy.InterestStrategy;
import org.wraith.strategy.SimpleInterestStrategy;
import org.wraith.util.IdGenerator;

public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepository repository;
    private final InterestStrategy interestStrategy;

    public CreditCardServiceImpl(CreditCardRepository repository,
                                 InterestStrategy interestStrategy) {
        this.repository = repository;
        this.interestStrategy = new SimpleInterestStrategy();
    }

    @Override
    public void onboardUser(String userId, String name, String email, String phoneNumber) {
        CreditCard card = new CreditCard(IdGenerator.generateId());
        User user = new User(userId, name, email,phoneNumber, card);
        repository.save(user);
    }

    @Override
    public void spend(String userId, double amount) {
        User user = validateUser(userId);
        CreditCard card = user.getCreditCard()
                ;
        if(card.getBalance() >= amount ) {
            card.setSpent(card.getSpent() + amount);
            card.setBalance(card.getBalance() - amount);
        } else {
            throw new IllegalArgumentException("Insufficient balance");
        }
        card.getTransactions().add(
                new Transaction(IdGenerator.generateId(), amount, TransactionType.DEBIT));
    }

    @Override
    public double getBalance(String userId) {
        return validateUser(userId).getCreditCard().getBalance();
    }

    @Override
    public void generateStatement(String userId) {
        User user = validateUser(userId);
        CreditCard card = user.getCreditCard();

        System.out.println("\nStatement for " + user.getName());
        for (Transaction t : card.getTransactions()) {
            System.out.println(t.getType() + " : " + t.getAmount());
        }
        System.out.println("Total Balance: " + card.getBalance());
        System.out.println("Total Spent Balance: " + card.getSpent());
    }

    @Override
    public synchronized void payBill(String userId, double amount) {
        User user = validateUser(userId);
        CreditCard card = user.getCreditCard();

        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");

        double spent = card.getSpent();

        if (amount > spent) {
            throw new IllegalArgumentException("Overpayment not allowed");
        }

        card.setSpent(spent - amount);
        card.setBalance(card.getBalance() + amount);

        card.getTransactions().add(
                new Transaction(IdGenerator.generateId(), amount, TransactionType.PAYMENT)
        );

        if (card.getSpent() > 0) {
            double interest = interestStrategy.calculate(card.getSpent());
            card.setSpent(card.getBalance() + interest);
            card.getTransactions().add(
                    new Transaction(IdGenerator.generateId(), interest, TransactionType.INTEREST)

            );
        }
    }

    private User validateUser(String userId) {
        User user = repository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

}
