package com.michael.attackpoint.log.loginfo;

/**
 * Class containing info on distance and unit of a log entry
 * @author Michael Laraia
 */
public class LogDistance extends LogInfoItem<Float>{
    // TODO allow conversion between units
    public float distance;
    public String unit;


    /**
     * Creates empty distance object
     */
    public LogDistance() {
        distance = 0;
        unit = "";
    }

    /**
     * Creates distance object
     * @param distance distance traveled during workout
     * @param unit unit of distance
     */
    public LogDistance(int distance, String unit) {
        this.distance = distance;
        this.unit = unit;
    }

    /**
     * Creates distance object
     * @param distance distance traveled during workout
     * @param unit unit of distance
     */
    public LogDistance(String distance, String unit) {
        if (!(distance == null || distance.equals(""))) {
            this.distance = Float.parseFloat(distance);
            this.unit = unit;
        } else {
            this.distance = 0;
            this.unit = "";
        }
    }

    /**
     * Creates distance object
     * @param distance distance traveled during workout
     * @param unit unit of distance
     */
    public LogDistance(float distance, String unit) {
        this.distance = distance;
        this.unit = unit;
    }

    /**
     * Creates distance object
     * @param distance combination of distance traveled and
     *                 the unit, seperated by a space
     */
    public LogDistance(String distance) {
        if (!(distance == null || distance.equals(""))) {
            String[] items = distance.split(" ");
            this.distance = Float.parseFloat(items[0]);
            this.unit = items[1];
        } else {
            this.distance = 0;
            this.unit = "";
        }
    }

    /**
     * Checks if distance is 0
     * @return
     */
    public boolean isEmpty() {
        if (distance == 0 || unit.equals("")) return true;
        else return false;
    }

    /**
     * Converts distance into human-readable string
     * @return
     */
    public String toString() {
        if (distance <= 0) return "";
        return Float.toString(distance) + " " + unit;
    }
}