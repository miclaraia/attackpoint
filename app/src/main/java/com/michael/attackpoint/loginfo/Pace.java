package com.michael.attackpoint.loginfo;

import android.text.format.*;

/**
 * Class managing pace of log entries
 */
public class Pace {
    private android.text.format.Time pace;
    private String unit;
    // TODO conversion between min/km and min/mi

    /**
     * creates empty Pace object
     */
    public Pace() {
        unit = "";
        pace = new android.text.format.Time();
        pace.set(0,0,0,0,0,0);
    }

    /**
     * creates Pace object from Duration and Distance objects
     * @param t duration of log entry
     * @param d distance traveled in workout
     */
    public Pace(Duration t, Distance d) {
        this.unit = d.unit;
        this.pace = calc(t, d);
    }

    /**
     * craetes Pace object from Time and Distance objects
     * @param time duration of log entry
     * @param distance distance traveled in workout
     */
    public Pace(android.text.format.Time time, Distance distance) {
        this.unit = distance.unit;
        set(calc(time, distance));
    }

    /**
     * sets pace value to specified pace
     * @param pace
     */
    public void set(android.text.format.Time pace) {
        this.pace = pace;
    }

    /**
     * Calculates pace given time and distance
     * @param t duration
     * @param d distance traveled
     * @return
     */
    private Time calc(Duration t, Distance d) {
        return calc(t.get(), d);
    }

    private Time calc(android.text.format.Time time, Distance distance) {
        // TODO make sure this calculates properly
        int h = time.hour;
        int m = time.minute;
        int s = time.second;

        int pace = (int) Math.floor((h*3600 + m*60 + s) / distance.distance);

        h = (int) pace / 3600;
        int r = pace % 3600;
        m = (int) r / 60;
        s = r % 60;
        android.text.format.Time out = new android.text.format.Time();
        out.set(s,m,h,0,0,0);
        return out;
    }

    /**
     * gets pace
     * @return
     */
    public android.text.format.Time get() {
        return pace;
    }

    /**
     * returns pace as formatted string
     * @return
     */
    public String toString() {
        String out;
        if (this.pace == null) {
            return "";
        }
        if (this.pace.hour == 0) {
            if (this.pace.minute == 0 && this.pace.second == 0) return "";
            out = pace.format("%M:%S");
        }
        else out = pace.format("%H:%M:%S");

        out += " / " + this.unit;
        return out;
    }
}
