package com.michael.attackpoint.log.loginfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class that manages the date used in representing a log entry.
 * Can parse date from attackpoint.org and formats it for use in
 * the app's log entries.
 * @author Michael Laraia
 */
public class Date {
    private static final String DATE_FORMAT = "ccc MMM d";
    private static final String JSON_FORMAT = "yyyy-MM-dd";
    //todo change to get date from link
    //private static final String LOG_FORMAT = "cccc MMM d #";
    private static final String LOG_PARSE = "'enddate-'yyyy-MM-dd";
    private static final String LOG_FORMAT_SESSION = "h a";

    private Calendar cal;
    private SimpleDateFormat sdf;

    public Date() {
        sdf = new SimpleDateFormat(DATE_FORMAT);
        cal = Calendar.getInstance();
    }

    public Date(String logDate, boolean json) {
        sdf = new SimpleDateFormat(DATE_FORMAT);
        cal = Calendar.getInstance();
        set(logDate);
    }

    /**
     * parses and sets date
     * @param logDate must be either in
     * the format 'ccc MMM d' used by the app or 'cccc MMM d #' used by
     * attackpoint.org
     */
    public void set(String logDate) {
        if (logDate.contains("enddate")) {
            SimpleDateFormat sdf = new SimpleDateFormat(LOG_PARSE);
            try {
                java.util.Date date = sdf.parse(logDate);
                cal.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            try {
                java.util.Date date = sdf.parse(logDate);
                cal.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void set(Calendar cal) {
        this.cal = cal;
    }

    /**
     * sets time of day of session
     * @param time must be in format 'h a'
     *             as used on attackpoint.org
     */
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

    public Calendar getDate() {
        return cal;
    }

    public String toString() {
        return sdf.format(cal.getTime());
    }

    public String toJSON() {
        SimpleDateFormat sdf = new SimpleDateFormat(JSON_FORMAT);
        return sdf.format(cal.getTime());
    }

    public void fromJSON(String json) {
        SimpleDateFormat sdf = new SimpleDateFormat(JSON_FORMAT);
        try {
            java.util.Date date = sdf.parse(json);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
