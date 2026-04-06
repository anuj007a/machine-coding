package com.wraith.strategy;

import com.wraith.model.Location;

public interface DistanceStrategy {
    double calculate(Location a, Location b);

}
