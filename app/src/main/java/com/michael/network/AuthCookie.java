package com.michael.network;

import android.util.Log;

import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by michael on 8/18/15.
 */
public class AuthCookie {
    private static final String DEBUG_TAG = "attackpoint.AuthCookie";
    private static final String EXPIRE_FORMAT = "ccc, dd-MMM-yyyy HH:mm:ss zzz";
    private static final String key = "login";
    private Date expire;
    private String token;
    private Preferences prefs = Singleton.getInstance().getPreferences();

    public AuthCookie() {
        //TODO load cookie from preferences
    }

    public AuthCookie(String cookie) {
        //TODO check if cookie already exists in preferences
        //set cookie
    }

    public AuthCookie(String token, Date expire) {
        //TODO check if cookie already exists in preferences
        //set cookie
    }

    // sets cookie given header map
    public void setCookie(Map<String, List<String>> headers) {
        setCookie(parseHeader(headers));
    }

    // Sets the objects token and expiration date, assumes right auth cookie was passed
    public void setCookie(String cookie) {
        String[] c = cookie.split(";");
        String token = c[0].substring(6);
        String expire = c[3].split("=")[1];

        Date e = parseExpire(expire);

        setCookie(token, e);
    }

    public void setCookie(String token, Date expire) {
        this.token = token;
        this.expire = expire;
    }

    public String getCookie() {
        // TODO
        return "";
    }

    // saves cookie to preferences
    public void save() {
        prefs.saveCookie(this.toString());
    }

    // reads cookie from preferences
    public void read() {
        this.setCookie(prefs.getCookie());
    }

    public void expire() {
        prefs.removeCookie();
    }

    // finds cookie in the header map
    public String parseHeader(Map<String, List<String>> headers) {
        List<String> cookies = headers.get("Set-Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                //todo remore temp string
                String temp = cookie.split("=")[0];
                if (cookie.split("=")[0].equals("login")) return cookie;
            }
        }
        return null;
    }

    // checks if cookie has expired
    public boolean checkTime() {
        Date now = Calendar.getInstance().getTime();
        int x = now.compareTo(expire);
        if (x > 0) {
            Log.d(DEBUG_TAG, "cookie expired");
            return false;
        } else return true;
    }

    public Date parseExpire(String time) {
        /*String day = time.substring(5,7);
        String month = time.substring(8,11);
        String year = time.substring(12,16);
        String hour = time.substring(17,19);
        String minute = time.substring(20,22);
        String second = time.substring(23,25);

        int D = Integer.parseInt(day);
        int M = parseMonth(month);
        int Y = Integer.parseInt(year);
        int h = Integer.parseInt(hour);import java.util.GregorianCalendar;
        int m = Integer.parseInt(minute);
        int s = Integer.parseInt(second);

        Time exp = new Time();*/
        SimpleDateFormat sdf = new SimpleDateFormat(EXPIRE_FORMAT);
        try {
            Date exp = sdf.parse(time);
            return exp;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        //exp.set(s,m,h,M,D,Y);
    }

    public int parseMonth(String month) {
        switch (month) {
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;
            case "Dec":
                return 12;
            default:
                return 0;
        }
    }

    //cookie string to be stored in preferences
    public String serialize() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("key", token);
        json.put("expire", new SimpleDateFormat(EXPIRE_FORMAT).format(expire));
        return json.toString();
    }

    public String toString() {
        return key + "=" + token + "; ";
    }


}
