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
                if (ct.s == 0) return "";
                format = "ss";
            }
            else format = "mm:ss";
        }
        else format = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(mItem.getTime());
    }

    @Override
    public String toFormString() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_FORM_OUT);
        return sdf.format(mItem.getTime());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
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

    /**
     * Creates empty duration object
     *//*
    public LogDuration() {
        mCalendar = Calendar.getInstance();
        mCalendar.set(0,0,0,0,0,0);
    }

    *//**
     * Creates duration object
     * @param time time string must be a properly formatted hh:mm:ss time
     *//*
    public LogDuration(String time) {
        mCalendar = Calendar.getInstance();

        int count = time.split(":").length;
        String format = FORMAT_NORMAL.substring((3 - count) * 3);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            java.util.Date d = sdf.parse(time);
            mCalendar.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    *//**
     * Creates duration object from android Calendar object
     * @param cal
     *//*
    public LogDuration(Calendar cal) {
        mCalendar = cal;
    }

    public void setCalendar(Calendar cal) {
        mCalendar = cal;
    }

    *//**
     * gets the current duration as Calendar object
     * @return
     *//*
    public Calendar getCalendar() {
        return mCalendar;
    }

    *//**
     * returns current duration as string
     * @return
     *//*
    public String toString() {
        String format;
        CalendarTime ct = new CalendarTime(mCalendar);
        if (ct.h == 0) {
            if (ct.m == 0) {
                if (ct.s == 0) return "";
                format = "ss";
            }
            else format = "mm:ss";
        }
        else format = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(mCalendar.getTime());
    }

    public String toFormString() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_FORM_OUT);
        return sdf.format(mCalendar.getTime());
    }

    public boolean isEmpty() {
        CalendarTime ct = new CalendarTime(mCalendar);
        if (ct.h == 0 && ct.m == 0 && ct.s == 0)
            return true;
        return false;
    }*/
}
