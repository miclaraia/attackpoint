package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class managing time and duration of log entries
 */
public class LogDuration extends LogInfoItem<Calendar> {
    private static final String FORMAT_JSON = "HH:mm:ss";
    private static final String FORMAT_FORM_OUT = "HHmmss";
    private static final String JSON = "duration";
    private static final String FORMAT_S = "s";
    private static final String FORMAT_M = "m:ss";
    private static final String FORMAT_H = "H:mm:ss";

    public LogDuration() {
        super();
    }

    public LogDuration(JSONObject json) {
        super(json);
    }

    public LogDuration(Calendar duration) {
        super();
        set(duration);
    }

    @Override
    public void onCreate() {
        mItem = Calendar.getInstance();
        mItem.set(0,0,0,0,0,0);
    }

    @Override
    public boolean isEmpty() {
        CalendarTime ct = new CalendarTime(mItem);
        if (ct.h == 0 && ct.m == 0 && ct.s == 0)
            return true;
        return false;
    }

    @Override
    public String toString() {
        String format;
        CalendarTime ct = new CalendarTime(mItem);
        if (ct.h == 0) {
            if (ct.m == 0) {
                if (ct.s == 0) format = FORMAT_H;
                else format = FORMAT_S;
            }
            else format = FORMAT_M;
        }
        else format = FORMAT_H;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(mItem.getTime());
    }

    @Override
    public String toFormString() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_FORM_OUT);
        return sdf.format(mItem.getTime());
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_JSON);
        try {
            json.put(JSON, sdf.format(mItem.getTime()));
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fromJSON(JSONObject json) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_JSON);
        try {
            String durationString = (String) json.get(JSON);
            Date date = sdf.parse(durationString);
            mItem.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static Calendar parseLog(String durationString) throws ParseException {
        int length = durationString.split(":").length;
        String format;
        switch (length) {
            case 1:
                format = "ss";
                break;
            case 2:
                format = "mm:ss";
                break;
            case 3:
                format = "HH:mm:ss";
                break;
            default:
                format = FORMAT_FORM_OUT;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(durationString));
        return cal;
    }
}
