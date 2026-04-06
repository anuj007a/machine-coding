package org.wraith.strategy;

import org.wraith.model.Location;

public interface DistanceStrategy {
    double calculate(Location a, Location b);

}
