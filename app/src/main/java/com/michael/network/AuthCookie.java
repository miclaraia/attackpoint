package com.michael.network;

import android.util.Log;
import android.widget.Toast;

import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
        Log.d(DEBUG_TAG, "Initializing AuthCookie, no constructor");
        setCookie();
    }

    public AuthCookie(Map<String, List<String>> headers) {
        Log.d(DEBUG_TAG, "Initializing AuthCookie, map constructor");
        setCookie(headers);
    }

    public AuthCookie(String cookie) {
        Log.d(DEBUG_TAG, "Initializing AuthCookie, string constructor");
        setCookie(cookie);
    }

    public AuthCookie(String t, Date e) {
        Log.d(DEBUG_TAG, "Initializing AuthCookie, var constructor");
        setCookie(t, e);
    }

    // sets cookie given header map
    public void setCookie(Map<String, List<String>> headers) {
        Log.d(DEBUG_TAG, "setting cookie from map");
        setCookie(parseHeader(headers));
    }

    // Sets the objects token and expiration date, assumes right auth cookie was passed
    public void setCookie(String cookie) {
        Log.d(DEBUG_TAG, "setting cookie from string");
        String token = parseCookie(cookie);
        Date e = parseExpire(cookie);
        setCookie(token, e);
    }

    public void setCookie() {
        Log.d(DEBUG_TAG, "setting cookie from read()");
        Map<String, Object> map = read();
        if (map != null) {
            String t = (String) map.get("key");
            Date e = (Date) map.get("expire");
            setCookie(t, e);
        }
    }

    //checks if oken and expire are valid and whether it has already expired
    //before setting the object variables
    public void setCookie(String t, Date e) {
        Log.d(DEBUG_TAG, "setting cookie from vars");
        if (checkVals(t, e) && checkTime(e)) {
            token = t;
            expire = e;
            save();
        }
    }

    // saves cookie to preferences
    public void save() {
        Log.d(DEBUG_TAG, "saving cookie to preferences");
        try {
            prefs.saveCookie(this.serialize());
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "JSONException trying to save cookie");
            e.printStackTrace();
            Toast.makeText(Singleton.getInstance().getContext(),
                    DEBUG_TAG + "JSONException saving cookie", Toast.LENGTH_LONG).show();
        }
    }

    // reads cookie from preferences
    private Map<String, Object> read() {
        Log.d(DEBUG_TAG, "reading cookie from preferences");
        try {
            //gets and parses json from preferences
            String cookie = prefs.getCookie();
            JSONObject json = new JSONObject(cookie);
            String t = (String) json.get("key");
            Log.v(DEBUG_TAG, "found cookie token: " + t);
            String ex = (String) json.get("expire");
            //makes sure existing cookies hasnt expired
            Date e = parseExpire(ex);
            if (checkVals(t, e) && checkTime(e)) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("key", t);
                map.put("expire", e);
                return map;
            } else {
                expire();
                return null;
            }
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "JSONException trying to read cookie");
            e.printStackTrace();
            prefs.removeCookie();
            Toast.makeText(Singleton.getInstance().getContext(),
                    DEBUG_TAG + "JSONException reading cookie", Toast.LENGTH_LONG).show();
            return null;
        } catch (NullPointerException e) {
            Log.e(DEBUG_TAG, "NullPointer trying to read cookie");
            e.printStackTrace();
            Toast.makeText(Singleton.getInstance().getContext(),
                    DEBUG_TAG + "NullPointer reading cookie", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    // removes cookie from preferences
    private void expire() {
        Log.d(DEBUG_TAG, "expiring cookie from preferences");
        prefs.removeCookie();
    }

    // finds cookie in the header map
    private String parseHeader(Map<String, List<String>> headers) {
        List<String> cookies = headers.get("Set-Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
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
            try {
                String[] c = cookie.split(";");
                String token = c[0].substring(6);
                return token;
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.d(DEBUG_TAG, "cookie not valid");
                e.printStackTrace();
                return "";
            }
        } else return "";
    }

    //pulls expiration date from cookie string
    private Date parseExpire(String cookie) {
        if (cookie != null && !cookie.equals("")) {
            String time = "";

            if (cookie.contains(";") || cookie.contains("expires")) {
                String[] c = cookie.split(";");
                for (int i = 0; i < c.length; i++) {
                    if (c[i].contains("expires")) {
                        time = c[i].split("=")[1];
                        break;
                    }
                }
                if (time.equals("")) return null;
            } else time = cookie;

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

    //cookie string to be stored in preferences
    private String serialize() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("key", token);
        json.put("expire", new SimpleDateFormat(EXPIRE_FORMAT).format(expire));
        return json.toString();
    }

    public String toString() {
        if (checkVals(token, expire) && checkTime(this.expire)) return key + "=" +  token + ";";
        return "";
    }
}
