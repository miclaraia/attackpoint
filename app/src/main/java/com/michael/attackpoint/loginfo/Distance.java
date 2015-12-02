package com.michael.attackpoint.loginfo;

/**
 * Class containing info on distance and unit of a log entry
 * @author Michael Laraia
 */
public class Distance {
    // TODO allow conversion between units
    public float distance;
    public String unit;


    /**
     * Creates distance object
     * @param distance distance traveled during workout
     * @param unit unit of distance
     */
    public Distance(int distance, String unit) {
        this.distance = distance;
        this.unit = unit;
    }

    /**
     * Creates distance object
     * @param distance distance traveled during workout
     * @param unit unit of distance
     */
    public Distance(String distance, String unit) {
        this.distance = Float.parseFloat(distance);
        this.unit = unit;
    }

    /**
     * Creates distance object
     * @param distance distance traveled during workout
     * @param unit unit of distance
     */
    public Distance(float distance, String unit) {
        this.distance = distance;
        this.unit = unit;
    }

    /**
     * Creates distance object
     * @param distance combination of distance traveled and
     *                 the unit, seperated by a space
     */
    public Distance(String distance) {
        String[] items = distance.split(" ");
        this.distance = Float.parseFloat(items[0]);
        this.unit = items[1];
    }

    public String toString() {
        return Float.toString(distance) + " " + unit;
    }
}