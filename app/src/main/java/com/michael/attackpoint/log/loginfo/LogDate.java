package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class that manages the date used in representing a log entry.
 * Can parse date from attackpoint.org and formats it for use in
 * the app's log entries.
 * @author Michael Laraia
 */
public class LogDate extends LogInfoItem<Calendar> {
    protected static final String FULL_FORMAT = "EEE MMM d - h a";
    protected static final String DATE_FORMAT = "EEE MMM d";
    protected static final String SESSION_FORMAT = "h a";
    protected static final String SESSION_FORMAT_FORM = "H";
    //todo change to get date from link
    protected static final String LOG_PARSE = "'enddate-'yyyy-MM-dd";
    protected static final String LOG_PARSE_SESSION = "hh a";

    public static final String JSON = "date";
    protected static final String JSON_FORMAT = "yyyy-MM-dd-kk:mm";
    //todo change to get date from link


    public LogDate() {
        super();
    }

    public LogDate(JSONObject json) {
        super(json);
    }

    public LogDate(Calendar date) {
        super();
        set(date);
    }

    @Override
    public void onCreate() {
        mItem = Calendar.getInstance();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public boolean isEmptySession() {
        int session = mItem.get(Calendar.HOUR_OF_DAY);
        if (session == 0) return true;
        return false;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "";
        SimpleDateFormat sdf;
        if (!isEmptySession()) sdf = new SimpleDateFormat(FULL_FORMAT);
        else sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(mItem.getTime());
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
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
            Date date = sdf.parse(dateString);
            mItem.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSession(int hour) {
        mItem.set(Calendar.HOUR_OF_DAY, hour);
    }

    public void setDate(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, mItem.get(Calendar.HOUR_OF_DAY));
        mItem = date;
    }

    public String getSession() {
        if (!isEmptySession()) {
            SimpleDateFormat sdf = new SimpleDateFormat(SESSION_FORMAT);
            return sdf.format(mItem.getTime());
        }
        return "";
    }

    public String getSession_form() {
        if (!isEmptySession()) {
            SimpleDateFormat sdf = new SimpleDateFormat(SESSION_FORMAT_FORM);
            return sdf.format(mItem.getTime());
        }
        return "-1";
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(mItem.getTime());
    }

    public static Calendar parseLogDate(String dateString) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(LOG_PARSE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(dateString));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal;
    }

    public static int parseLogSession(String sessionString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(LOG_PARSE_SESSION);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(sessionString));
        return cal.get(Calendar.HOUR_OF_DAY);
    }
}
