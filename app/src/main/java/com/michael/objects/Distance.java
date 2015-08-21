package com.michael.objects;

/**
 * Created by michael on 8/6/15.
 */
public class Distance {
    public float distance;
    public String unit;


    public Distance(int distance, String unit) {
        this.distance = distance;
        this.unit = unit;
    }

    public Distance(String distance, String unit) {
        this.distance = Float.parseFloat(distance);
        this.unit = unit;
    }

    public Distance(float distance, String unit) {
        this.distance = distance;
        this.unit = unit;
    }

    public String toString() {
        return Float.toString(distance) + " " + unit;
    }
}