package com.wraith.strategy;

import com.wraith.model.Location;

public class HaversineDistance implements DistanceStrategy {
    private static final double R = 6371; // km
    @Override
    public double calculate(Location a, Location b) {
        double lat1 = Math.toRadians(a.getLat());
        double lon1 = Math.toRadians(a.getLon());
        double lat2 = Math.toRadians(b.getLat());
        double lon2 = Math.toRadians(b.getLon());
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double h = Math.sin(dLat/2)*Math.sin(dLat/2) +
                Math.cos(lat1)*Math.cos(lat2) *
                        Math.sin(dLon/2)*Math.sin(dLon/2);

        return 2 * R * Math.asin(Math.sqrt(h));
    }



}
