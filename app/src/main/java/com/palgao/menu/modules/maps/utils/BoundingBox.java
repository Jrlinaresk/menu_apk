package com.palgao.menu.modules.maps.utils;

public class BoundingBox {
    private double north;
    private double east;
    private double south;
    private double west;

    public BoundingBox(double north, double east, double south, double west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    public double getNorth() {
        return north;
    }

    public double getEast() {
        return east;
    }

    public double getSouth() {
        return south;
    }

    public double getWest() {
        return west;
    }
}

