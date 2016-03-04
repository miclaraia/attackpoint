package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

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
        if (mItem.getDistanceStandard() <= 0) return true;
        return false;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "";
        return mItem.getDistance().toString() + " " + mItem.getUnit().toNickname();
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
        try {
            json.put(JSON_DISTANCE, mItem.getDistanceStandard().toString());
            json.put(JSON_UNIT, mItem.getUnit().toString());
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

            double distance = Double.parseDouble(d);

            mItem.setDistance(distance);
            mItem.setUnit(Unit.UnitManager.getUnit(u));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Distance {
        private Double mDistance;
        private Unit mUnit;

        public Unit getUnit() {
            return mUnit;
        }

        public Double getDistance() {
            return mUnit.convert(mDistance);
        }

        protected Double getDistanceStandard() {
            return mDistance;
        }

        public void setUnit(Unit unit) {
            mUnit = unit;
        }

        public void setDistance(Double distance) {
            mDistance = distance;
        }

        public Distance() {
            mDistance = Double.valueOf(0);
            mUnit = Unit.UnitManager.getDefault();
        }

        public Distance(double distance, String unit) {
            mDistance = distance;
            mUnit = Unit.UnitManager.getUnit(unit);
        }
    }
}