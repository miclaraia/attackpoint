package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Formatter;

/**
 * Class containing info on distance and unit of a log entry
 * @author Michael Laraia
 */
public class LogClimb extends LogInfoItem<LogClimb.Climb> {
    private static final String JSON_CLIMB = "climb_climb";
    private static final String JSON_UNIT = "climb_unit";
    private static final String FORMAT = "+%d%s";

    @Override
    public void onCreate() {
        mItem = new Climb();
    }

    @Override
    public boolean isEmpty() {
        if (mItem.climb == 0) return true;
        return false;
    }

    @Override
    public String toString() {
        Formatter f = new Formatter();
        return f.format(FORMAT, mItem.climb, mItem.unit).toString();
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
        try {
            json.put(JSON_CLIMB, mItem.climb);
            json.put(JSON_UNIT, mItem.unit);
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fromJSON(JSONObject json) {
        try {
            int c = (int) json.get(JSON_CLIMB);
            String u = (String) json.get(JSON_UNIT);

            mItem.climb = c;
            mItem.unit = u;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static Climb parseClimb(String climbString) {
        Climb climb = new Climb();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_unit = new StringBuilder();

        boolean n_found = false;
        for (int i = 0; i < climbString.length(); i++) {
            char c = climbString.charAt(i);
            if (c >= 48 && c<= 57) {
                n_found = true;
                sb.append(c);
            } else if (n_found) {
                sb_unit.append(c);
            }
        }

        if (n_found) {
            climb.climb = Integer.parseInt(sb.toString());
            climb.unit = sb_unit.toString();
        }

        return climb;
    }

    public static class Climb {
        public static final String UNIT_METRIC = "m";
        public static final String UNIT_IMPERIAL = "ft";
        public static final String UNIT_DEFAULT = UNIT_METRIC;

        public int climb;
        public String unit;

        public Climb() {
            climb = 0;
            unit = UNIT_DEFAULT;
        }

        public Climb(int climb, String unit) {
            this.climb = climb;
            this.unit = unit;

        }
    }
    /*// TODO allow conversion between units
    public int distance;
    public static final String UNIT = "m";
    public static final String JSON = "climb";


    *//**
     * Creates empty distance object
     *//*
    public LogClimb() {
        distance = 0;
    }

    *//**
     * Creates distance object
     * @param distance distance traveled during workout
     *//*
    public LogClimb(int distance) {
        this.distance = distance;
    }

    *//**
     * Creates distance object
     * @param distance distance traveled during workout
     *//*
    public LogClimb(String distance) {
        if (!(distance == null || distance.equals(""))) {
            if (distance.contains("+")) {
                int l = distance.length();
                distance = distance.substring(1, l - 1);
            }
            this.distance = Integer.parseInt(distance);
        } else {
            this.distance = 0;
        }
    }

    public LogClimb(JSONObject json) {
        fromJSON(json);
    }

    *//**
     * Checks if distance is 0
     * @return
     *//*
    public boolean isEmpty() {
        if (distance == 0) return true;
        else return false;
    }

    *//**
     * Converts distance into human-readable string
     * @return
     *//*
    public String toString() {
        if (distance <= 0) return "";
        return "+" + distance + UNIT;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON, distance);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return json;
        }
    }

    public void fromJSON(JSONObject json) {
        try {
            int d = (int) json.get(JSON);
            distance = d;
        } catch (JSONException e) {
            e.printStackTrace();
            distance = 0;
        }
    }*/
}