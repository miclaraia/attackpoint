package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class that manages the date used in representing a log entry.
 * Can parse date from attackpoint.org and formats it for use in
 * the app's log entries.
 * @author Michael Laraia
 */
public class Date extends LogInfoItem<Calendar> {
    private static final String DATE_FORMAT = "ccc MMM d";
    private static final String JSON_FORMAT = "yyyy-MM-dd";
    //todo change to get date from link
    //private static final String LOG_FORMAT = "cccc MMM d #";
    private static final String LOG_PARSE = "'enddate-'yyyy-MM-dd";
    public static final String JSON = "date";

    /**
     * parses and sets date
     * @param logDate must be either in
     * the format 'ccc MMM d' used by the app or 'cccc MMM d #' used by
     * attackpoint.org
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

    */

    public static Calendar parseLog(String dateString) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(LOG_PARSE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(dateString));
        return cal;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(mItem.getTime());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat(JSON_FORMAT);
        try {
            json.put(JSON, sdf.format(mItem.getTime()));
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fromJSON(JSONObject json) {
        SimpleDateFormat sdf = new SimpleDateFormat(JSON_FORMAT);
        try {
            String dateString = (String) json.get(JSON);
            java.util.Date date = sdf.parse(dateString);
            mItem.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onCreate() {
        mItem = Calendar.getInstance();
    }
}
