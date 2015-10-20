package com.michael.network;

import android.content.ContentValues;
import android.util.Log;

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
 * Created by michael on 10/15/15.
 */
public class User {

    private static final String DEBUG_TAG = "attackpoint.User";
    private static final String EXPIRE_FORMAT = "ccc, dd-MMM-yyyy HH:mm:ss zzz";

    private int id;
    private String user;
    private String token;
    private Date expire;

    public User(String user, Map<String, List<String>> headers) {
        String cookie = parseHeader(headers);
        if (cookie != null) {
            String t = findToken(cookie);
            String e = findExpire(cookie);

            this.user = user;
            this.token = t;

            try {
                this.expire = parseDate(e);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

    public User(int id, String user, String token, Date expire) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expire = expire;
    }

    public User(int id, String user, String token, String expire) {
        this.id = id;
        this.user = user;
        this.token = token;
        try {
            this.expire = parseDate(expire);
        } catch (ParseException e) {
            e.printStackTrace();
            this.expire = null;
        }
    }

    public User(String cookie) {

    }

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Date getExpire() {
        return expire;
    }

    public String getExpireString() {
        return dateToString(expire);
    }

    public void setId(long id) {
        this.id = (int) id;
    }

    public boolean isExpired() {
        Date now = Calendar.getInstance().getTime();
        int x = now.compareTo(expire);
        if (x > 0) {
            Log.d(DEBUG_TAG, "cookie expired");
            return false;
        } else return true;
    }

    public ContentValues storeSQL() {
        //Map<String, String> values = new HashMap<String, String>();
        ContentValues values = new ContentValues();
        values.put(UserDbHelper.COLUMN_NAME, user);
        values.put(UserDbHelper.COLUMN_TOKEN, token);
        values.put(UserDbHelper.COLUMN_EXPIRE, dateToString(expire));
        return values;
    }

    private String findToken(String cookie) throws ArrayIndexOutOfBoundsException {
        if (cookie != null && !cookie.equals("")) {
            String[] c = cookie.split(";");
            String token = c[0].substring(6);
            return token;
        } else return "";
    }

    //pulls expiration date from cookie string
    private String findExpire(String cookie) {
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
            return time;
        } else return null;
    }

    private String parseHeader(Map<String, List<String>> headers) {
        List<String> cookies = headers.get("Set-Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.split("=")[0].equals("login")) return cookie;
            }
        }
        return null;
    }

    private String dateToString(Date date) {
        String s = new SimpleDateFormat(EXPIRE_FORMAT).format(date);
        return s;
    }

    private Date parseDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(EXPIRE_FORMAT);
        Date d = sdf.parse(date);
        return d;
    }

    public String toString() {
        return "login=" + token;
    }
}
