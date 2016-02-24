package com.michael.attackpoint.log.loginfo;

import android.text.format.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class managing pace of log entries
 */
public class Pace {
    private Calendar pace;
    private String unit;
    // TODO conversion between min/km and min/mi

    /**
     * creates empty Pace object
     */
    public Pace() {
        unit = "";
        pace = Calendar.getInstance();
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
    public Pace(Calendar cal, Distance distance) {
        this.unit = distance.unit;
        set(calc(cal, distance));
    }

    /**
     * sets pace value to specified pace
     * @param pace
     */
    public void set(Calendar pace) {
        this.pace = pace;
    }

    /**
     * Calculates pace given time and distance
     * @param t duration
     * @param d distance traveled
     * @return
     */
    private Calendar calc(Duration t, Distance d) {
        return calc(t.getCalendar(), d);
    }

    private Calendar calc(Calendar time, Distance distance) {
        // TODO make sure this calculates properly
        CalendarTime ct = new CalendarTime(time);

        int pace = (int) Math.floor((ct.h*3600 + ct.m*60 + ct.s) / distance.distance);

        int h = (int) pace / 3600;
        int r = pace % 3600;
        int m = (int) r / 60;
        int s = r % 60;

        Calendar out = Calendar.getInstance();
        out.set(Calendar.HOUR_OF_DAY, h);
        out.set(Calendar.MINUTE, m);
        out.set(Calendar.SECOND, s);
        return out;
    }

    /**
     * gets pace
     * @return
     */
    public Calendar get() {
        return pace;
    }

    /**
     * returns pace as formatted string
     * @return
     */
    public String toString() {
        String format;
        CalendarTime ct = new CalendarTime(pace);
        if (this.pace == null) {
            return "";
        }
        if (ct.h == 0) {
            if (ct.m == 0 && ct.s == 0) return "";
            format = "mm:ss";
        }
        else format = "HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String out = sdf.format(pace.getTime());
        out += " / " + this.unit;
        return out;
    }

    public boolean isEmpty() {
        CalendarTime ct = new CalendarTime(pace);
        if (ct.h == 0 && ct.m == 0 && ct.s == 0)
            return true;
        return false;
    }
}
