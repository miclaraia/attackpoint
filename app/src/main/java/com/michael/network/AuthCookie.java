package com.michael.network;

import android.util.Log;
import android.widget.Toast;

import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
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
        read();
    }

    public AuthCookie(Map<String, List<String>> headers) {

    }

    public AuthCookie(String cookie) {
        //TODO check if cookie already exists in preferences
        if (read()) {
            String t = parseCookie(cookie);
            Date e = parseExpire(cookie);
            if (checkVals(t, e)) {
                if (e.compareTo(expire) > 1) {
                    setCookie(t, e);
                    save();
                }
            }
        }
        setCookie(cookie);
    }

    public AuthCookie(String t, Date e) {
        //TODO check if cookie already exists in preferences
        if (read() && checkVals(t, e)) {
            if (e.compareTo(expire) > 1) {
                setCookie(t, e);
                save();
            }
        }
    }

    // sets cookie given header map
    public void setCookie(Map<String, List<String>> headers) {
        setCookie(parseHeader(headers));
    }

    // Sets the objects token and expiration date, assumes right auth cookie was passed
    public void setCookie(String cookie) {
        String token = parseCookie(cookie);
        Date e = parseExpire(cookie);
        setCookie(token, e);
    }

    //checks if oken and expire are valid and whether it has already expired
    //before setting the object variables
    public void setCookie(String t, Date e) {
        if (read()) {
            if (checkVals(t, e) && checkTime(e) && e.compareTo(expire) > 0) {
                token = t;
                expire = e;
            }
        } else if (checkVals(t, e) && checkTime(e)) {
            token = t;
            expire = e;
        } else {
            token = "";
            expire = null;
        }
    }

    // saves cookie to preferences
    public void save() {
        if (checkTime(expire)) {
            try {
                prefs.saveCookie(this.serialize());
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Singleton.getInstance().getContext(),
                        DEBUG_TAG + "JSONException saving cookie", Toast.LENGTH_LONG).show();
            }
        }
    }

    // reads cookie from preferences
    private boolean read() {
        try {
            JSONObject json = new JSONObject(prefs.getCookie());
            String token = (String) json.get("key");
            String e = (String) json.get("expire");
            Date expire = parseExpire(e);
            if (checkTime(expire)) {
                setCookie(token, expire);
                return true;
            } else {
                expire();
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Singleton.getInstance().getContext(),
                    DEBUG_TAG + "JSONException reading cookie", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    // removes cookie from preferences
    private void expire() {
        prefs.removeCookie();
    }

    // finds cookie in the header map
    private String parseHeader(Map<String, List<String>> headers) {
        List<String> cookies = headers.get("Set-Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                //todo remove temp string
                String temp = cookie.split("=")[0];
                if (cookie.split("=")[0].equals("login")) return cookie;
            }
        }
        return null;
    }

    // checks if time now is past expiration date
    private boolean checkTime(Date exp) {
        Date now = Calendar.getInstance().getTime();
        int x = now.compareTo(exp);
        if (x > 0) {
            Log.d(DEBUG_TAG, "cookie expired");
            return false;
        } else return true;
    }

    private boolean checkVals(String t, Date e) {
        if (t == null || t.equals("") || e == null) return false;
        return true;
    }

    //pulls token out of cookie string
    private String parseCookie(String cookie) {
        if (cookie != null && !cookie.equals("")) {
            String[] c = cookie.split(";");
            String token = c[0].substring(6);
            return token;
        } else return "";
    }

    //pulls expiration date from cookie string
    private Date parseExpire(String cookie) {
        if (cookie != null && !cookie.equals("")) {
            String[] c = cookie.split(";");
            String time = c[3].split("=")[1];
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
        } else return null;
    }

    private int parseMonth(String month) {
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
    private String serialize() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("key", token);
        json.put("expire", new SimpleDateFormat(EXPIRE_FORMAT).format(expire));
        return json.toString();
    }

    public String toString() {
        if (checkTime(this.expire)) return key + "=" +  token + ";";
        return "";
    }


}
