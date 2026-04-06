package org.wraith.strategy;

public class SimpleInterestStrategy implements InterestStrategy {
    private static final double RATE = 0.02; // 2%

    @Override
    public double calculate(double remainingAmount) {
        return remainingAmount * RATE;
    }
}
