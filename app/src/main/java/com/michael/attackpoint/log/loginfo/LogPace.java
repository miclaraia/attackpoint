package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class managing pace of log entries
 */
public class LogPace extends LogInfoItem<LogPace.Pace> {
    private static final String FORMAT = "m''ss";
    private static final String JSON_PACE = "pace_pace";
    private static final String JSON_UNIT = "pace_unit";

    public LogPace() {
        super();
    }

    public LogPace(JSONObject json) {
        super(json);
    }

    public LogPace(Pace pace) {
        super();
        set(pace);
    }

    @Override
    public void onCreate() {
        mItem = new Pace();
    }

    @Override
    public boolean isEmpty() {
        CalendarTime ct = new CalendarTime(mItem.pace);
        if (ct.h == 0 && ct.m == 0 && ct.s == 0) return true;
        return false;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        String pace = sdf.format(mItem.pace.getTime());
        pace += "/" + mItem.unit;
        return pace;
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        try {
            json.put(JSON_PACE, sdf.format(mItem.pace.getTime()));
            json.put(JSON_UNIT, mItem.unit);
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fromJSON(JSONObject json) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        try {
            String paceString = (String) json.get(JSON_PACE);
            Date date = sdf.parse(paceString);
            mItem.pace.setTime(date);

            String unit = (String) json.get(JSON_UNIT);
            mItem.unit = unit;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static Pace calcPace(LogDuration duration, LogDistance distance) {
        Pace pace = new Pace();
        LogDistance.Distance d = distance.get();

        pace.unit = d.unit;

        CalendarTime ct = new CalendarTime(duration.get());

        /**
         * Calculates pace based on seconds/distance initially
         * because int operations are easier to deal with than floats.
         * Then boils everything down to min/unit
         */
        int p = (int) Math.floor((ct.h*3600 + ct.m*60 + ct.s) / d.distance);
        int m = p / 60;
        int s = p % 60;

        pace.pace.set(Calendar.MINUTE, m);
        pace.pace.set(Calendar.SECOND, s);

        return pace;

        /*int pace = (int) Math.floor((ct.h*3600 + ct.m*60 + ct.s) / distance.distance);

        int h = (int) pace / 3600;
        int r = pace % 3600;
        int m = (int) r / 60;
        int s = r % 60;

        Calendar out = Calendar.getInstance();
        out.set(Calendar.HOUR_OF_DAY, h);
        out.set(Calendar.MINUTE, m);
        out.set(Calendar.SECOND, s);
        return out;*/

    }

    public static class Pace {
        public Calendar pace;
        public String unit;

        public Pace() {
            pace = Calendar.getInstance();
            pace.set(0,0,0,0,0,0);
            unit = LogDistance.Distance.UNIT_DEFAULT;
        }

        public Pace(Calendar pace, String unit) {
            this.pace = pace;
            this.unit = unit;
        }
    }

    /*private Calendar pace;
    private String unit;
    // TODO conversion between min/km and min/mi

    *//**
     * creates empty Pace object
     *//*
    public LogPace() {
        unit = "";
        pace = Calendar.getInstance();
        pace.set(0,0,0,0,0,0);
    }

    *//**
     * creates Pace object from Duration and Distance objects
     * @param t duration of log entry
     * @param d distance traveled in workout
     *//*
    public LogPace(LogDuration t, LogDistance d) {
        this.unit = d.unit;
        this.pace = calc(t, d);
    }

    *//**
     * craetes Pace object from Time and Distance objects
     * @param cal duration of log entry
     * @param distance distance traveled in workout
     *//*
    public LogPace(Calendar cal, LogDistance distance) {
        this.unit = distance.unit;
        set(calc(cal, distance));
    }

    *//**
     * sets pace value to specified pace
     * @param pace
     *//*
    public void set(Calendar pace) {
        this.pace = pace;
    }

    *//**
     * Calculates pace given time and distance
     * @param t duration
     * @param d distance traveled
     * @return
     *//*
    private Calendar calc(LogDuration t, LogDistance d) {
        return calc(t.getCalendar(), d);
    }

    private Calendar calc(Calendar time, LogDistance distance) {
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

    *//**
     * gets pace
     * @return
     *//*
    public Calendar get() {
        return pace;
    }

    *//**
     * returns pace as formatted string
     * @return
     *//*
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
    }*/
}
