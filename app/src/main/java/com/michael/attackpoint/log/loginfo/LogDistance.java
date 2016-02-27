package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class containing info on distance and unit of a log entry
 * @author Michael Laraia
 */
public class LogDistance extends LogInfoItem<LogDistance.Distance>{

    public static final String JSON_DISTANCE = "duration";
    public static final String JSON_UNIT = "unit";

    @Override
    public void onCreate() {
        mItem = new Distance();
    }

    @Override
    public boolean isEmpty() {
        if (mItem.distance <= 0) return true;
        return false;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "";
        return Float.toString(mItem.distance) + " " + mItem.unit;
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
        try {
            json.put(JSON_DISTANCE, mItem.distance);
            json.put(JSON_UNIT, mItem.unit);
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fromJSON(JSONObject json) {
        try {
            String d = (String) json.get(JSON_DISTANCE);
            String u = (String) json.get(JSON_UNIT);
            float distance = Float.parseFloat(d);
            mItem.distance = distance;
            mItem.unit = u;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public class Distance {
        public static final String UNIT_KM = "kilometer";
        public static final String UNIT_MI = "mile";
        public static final String UNIT_DEFAULT = UNIT_KM;

        public float distance;
        public String unit;

        public Distance() {
            distance = 0;
            unit = UNIT_DEFAULT;
        }

        public Distance(float distance, String unit) {
            this.distance = distance;
            this.unit = unit;
        }
    }
}