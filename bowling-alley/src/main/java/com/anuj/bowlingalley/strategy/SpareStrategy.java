package com.anuj.bowlingalley.strategy;

public class SpareStrategy implements Strategy{
    private static final Integer SPARE_BONUS = 5;

    @Override
    public int bonus() {
        return SPARE_BONUS;
    }
}
