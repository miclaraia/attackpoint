package com.michael.attackpoint.log.loginfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class managing time and duration of log entries
 */
public class Duration {
    private static final String FORMAT_NORMAL = "HH:mm:ss";
    private static final String FORMAT_FORM_OUT = "HHmmss";
    private Calendar mCalendar;

    /**
     * Creates empty duration object
     */
    public Duration() {
        mCalendar = Calendar.getInstance();
        mCalendar.set(0,0,0,0,0,0);
    }

    /**
     * Creates duration object
     * @param time time string must be a properly formatted hh:mm:ss time
     */
    public Duration(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_NORMAL);
        try {
            java.util.Date d = sdf.parse(time);
            mCalendar.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates duration object from android Calendar object
     * @param cal
     */
    public Duration(Calendar cal) {
        mCalendar = cal;
    }

    /**
     * gets the current duration as Calendar object
     * @return
     */
    public Calendar getCalendar() {
        return mCalendar;
    }

    /**
     * returns current duration as string
     * @return
     */
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
    }
}
