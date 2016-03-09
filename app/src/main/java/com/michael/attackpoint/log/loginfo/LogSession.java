package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by michael on 3/9/16.
 */
public class LogSession extends LogInfoItem<Calendar> {
    private static final String FORMAT = "hh:mm a";
    private static final String FORMAT_FORM_OUT = "H";
    private static final String JSON_FORMAT = "kk:mm";
    //todo change to get date from link
    private static final String LOG_PARSE = "hh a";
    public static final String JSON = "session";

    public LogSession() {
        super();
    }

    public LogSession(JSONObject json) {
        super(json);
    }

    public LogSession(Calendar date) {
        super();
        set(date);
    }

    @Override
    public void onCreate() {
        mItem = Calendar.getInstance();
    }

    @Override
    public boolean isEmpty() {
        if (mItem.get(Calendar.HOUR_OF_DAY) == 0) return true;
        return false;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.format(mItem.getTime());
    }

    public String toFormString() {
        if (isEmpty()) return "-1";
        else {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_FORM_OUT);
            return sdf.format(mItem.getTime());
        }
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

    public static Calendar parseLog(String dateString) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(LOG_PARSE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(dateString));
        return cal;
    }
}
