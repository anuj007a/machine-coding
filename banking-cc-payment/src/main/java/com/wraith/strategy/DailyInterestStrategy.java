package org.wraith.strategy;

public class DailyInterestStrategy implements InterestStrategy {
    private static final double DAILY_RATE = 0.0005; // 0.05% daily

    @Override
    public double calculate(double remainingAmount) {
        return remainingAmount * DAILY_RATE;
    }

}
