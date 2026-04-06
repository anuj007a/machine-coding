package com.wraith.model;

public class Location {
    private final double lat;
    private final double lon;
    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
