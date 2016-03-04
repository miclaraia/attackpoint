package com.michael.attackpoint.log.loginfo;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class containing info on distance and unit of a log entry
 * @author Michael Laraia
 */
public class LogDistance extends LogInfoItem<LogDistance.Distance> {

    public static final String JSON_DISTANCE = "distance_distance";
    public static final String JSON_UNIT = "distance_unit";

    public LogDistance() {
        super();
    }

    public LogDistance(JSONObject json) {
        super(json);
    }

    public LogDistance(Distance distance) {
        super();
        set(distance);
    }

    public LogDistance(float distance, String unit) {
        super();
        set(new Distance(distance, unit));
    }

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
        return Float.toString(mItem.distance) + " " + mItem.unit.getShortUnit();
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
        try {
            json.put(JSON_DISTANCE, mItem.distance.toString());
            json.put(JSON_UNIT, mItem.unit.toString());
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
            mItem.unit = new Unit(u);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Distance {
        public Float distance;
        public Unit unit;

        public Distance() {
            distance = Float.valueOf(0);
            unit = new Unit();
        }

        public Distance(float distance, String unit) {
            this.distance = distance;
            this.unit = new Unit(unit);
        }
    }
}