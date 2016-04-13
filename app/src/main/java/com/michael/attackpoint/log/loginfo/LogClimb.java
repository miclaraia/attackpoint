package com.michael.attackpoint.log.loginfo;

import android.support.annotation.VisibleForTesting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Formatter;

/**
 * Class containing info on distance and unit of a log entry
 * @author Michael Laraia
 */
public class LogClimb extends LogInfoItem<LogClimb.Climb> {
    protected static final String JSON_CLIMB = "climb_climb";
    protected static final String JSON_UNIT = "climb_unit";
    private static final String FORMAT = "+%d%s";

    public LogClimb() {
        super();
    }

    public LogClimb(JSONObject json) {
        super(json);
    }

    public LogClimb(Climb climb) {
        super();
        set(climb);
    }

    @Override
    public void onCreate() {
        mItem = new Climb();
    }

    @Override
    public boolean isEmpty() {
        if (mItem.getClimb() == 0) return true;
        return false;
    }

    @Override
    public String toString() {
        Formatter f = new Formatter();
        return f.format(FORMAT, mItem.getClimb(), mItem.getUnit()).toString();
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
        try {
            json.put(JSON_CLIMB, mItem.getClimb());
            json.put(JSON_UNIT, mItem.getUnit());
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

            mItem.setClimb(c);
            mItem.setUnit(u);
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
            climb.setClimb(Integer.parseInt(sb.toString()));
            climb.setUnit(sb_unit.toString());
        }

        return climb;
    }

    public void setClimb(int climb) {
        mItem.setClimb(climb);
    }

    public void setUnit(String unit) {
        mItem.setUnit(unit);
    }

    public static class Climb {
        public static final String UNIT_METRIC = "m";
        public static final String UNIT_IMPERIAL = "ft";
        public static final String UNIT_DEFAULT = UNIT_METRIC;

        private int mClimb;
        private String mUnit;

        public Climb() {
            mClimb = 0;
            mUnit = UNIT_DEFAULT;
        }

        public Climb(int climb) {
            mClimb = climb;
            mUnit = UNIT_DEFAULT;
        }

        public Climb(int climb, String unit) {
            mClimb = climb;
            mUnit = unit;

        }

        public void setClimb(int climb) {
            mClimb = climb;
        }

        public void setUnit(String unit) {
            mUnit = unit;
        }

        public int getClimb() {
            return mClimb;
        }

        public String getUnit() {
            return mUnit;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Climb) {
                Climb c = (Climb) o;
                if (c.mClimb == this.mClimb && c.mUnit.equals(this.mUnit)) return true;
            }
            return false;
        }
    }
    //TODO allow conversion between units
}