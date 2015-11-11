package com.michael.objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by michael on 11/11/15.
 */
public class Date {

    private static final String DATE_FORMAT = "ccc MMM d";
    private static final String LOG_FORMAT = "cccc MMM d #";
    private static final String LOG_FORMAT_SESSION = "h a";
    private Calendar cal;
    private SimpleDateFormat sdf;

    public Date() {
        sdf = new SimpleDateFormat(DATE_FORMAT);
    }

    public Date(String logDate) {
        sdf = new SimpleDateFormat(DATE_FORMAT);
        set(logDate);
    }

    public void set(String logDate) {
        cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(LOG_FORMAT);
        try {
            java.util.Date date = sdf.parse(logDate);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setSession(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(LOG_FORMAT_SESSION);
        try {
            Calendar parsed = Calendar.getInstance();
            parsed.setTime(sdf.parse(time));
            cal.set(Calendar.HOUR_OF_DAY, parsed.get(Calendar.HOUR_OF_DAY));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return sdf.format(cal.getTime());
    }
}
