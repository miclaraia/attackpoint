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
    protected static final String JSON_PACE = "pace_pace";
    protected static final String JSON_UNIT = "pace_unit";

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
        CalendarTime ct = new CalendarTime(mItem.getPaceStandard());
        if (ct.h == 0 && ct.m == 0 && ct.s == 0) return true;
        return false;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        String pace = sdf.format(mItem.getPace().getTime());
        pace += "/" + mItem.getUnit().toNickname();
        return pace;
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        try {
            json.put(JSON_PACE, sdf.format(mItem.getPace().getTime()));
            json.put(JSON_UNIT, mItem.getUnit().toString());
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
            mItem.setPace(date);

            String unit = (String) json.get(JSON_UNIT);
            mItem.setUnit(Unit.UnitManager.getUnit(unit));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static Pace calcPace(LogDuration duration, LogDistance distance) {
        Pace pace = new Pace();
        LogDistance.Distance d = distance.get();

        pace.setUnit(d.getUnit());

        CalendarTime ct = new CalendarTime(duration.get());

        /**
         * Calculates pace based on seconds/distance initially
         * because int operations are easier to deal with than floats.
         * Then boils everything down to min/unit
         */
        int p = (int) Math.floor(paceToSeconds(ct) / d.getDistanceStandard());

        pace.setPace(secondsToPace(p));
        return pace;
    }
    
    public static int paceToSeconds(CalendarTime pace) {
        int seconds = pace.h*3600 + pace.m*60 + pace.s;
        return seconds;
    }
    
    public static Calendar secondsToPace(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;

        Calendar pace = Calendar.getInstance();
        pace.set(Calendar.MINUTE, m);
        pace.set(Calendar.SECOND, s);

        return pace;
    }

    public static class Pace {
        private Calendar mPace;
        private Unit mUnit;

        public Pace() {
            mUnit = Unit.UnitManager.getDefault();
            mPace = Calendar.getInstance();
            mPace.set(0,0,0,0,0,0);
        }

        public Pace(Calendar pace, String unit) {
            mUnit = Unit.UnitManager.getUnit(unit);
            mPace = mUnit.standard(pace);
        }

        public Calendar getPace() {
            return mUnit.convert(mPace);
        }

        protected Calendar getPaceStandard() {
            return mPace;
        }

        public Unit getUnit() {
            return mUnit;
        }

        public void setPace(Calendar pace) {
            mPace = mUnit.standard(pace);
        }

        public void setPace(Date time) {
            mPace.setTime(time);
            mPace = mUnit.standard(mPace);
        }

        public void setUnit(Unit unit) {
            mUnit = unit;
        }
    }
}
