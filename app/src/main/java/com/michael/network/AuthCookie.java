package com.michael.network;

import android.text.format.Time;

import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;

import java.util.List;
import java.util.Map;

/**
 * Created by michael on 8/18/15.
 */
public class AuthCookie {
    private static final String DEBUG_TAG = "attackpoint.AuthCookie";
    private static final String key = "login";
    private Time expire;
    private String token;
    private Preferences prefs = Singleton.getInstance().getPreferences();

    public String findCookie(Map<String, List<String>> headers) {
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

    public void setCookie(Map<String, List<String>> headers) {
        setCookie(findCookie(headers));
    }

    // Sets the objects token and expiration date, assumes right auth cookie was passed
    public void setCookie(String cookie) {
        String[] c = cookie.split(";");
        String token = c[0].substring(6);
        String expire = c[3].split("=")[1];

        //TODO REMOVE TEMP
        setTime(expire);

        this.token = token;
        //this.expire = expire;
    }

    public void save() {
        prefs.saveCookie(this.toString());
    }

    public void read() {
        this.setCookie(prefs.getCookie());
    }

    public boolean checkTime(Time time) {
        //Time.no
        return false;
    }

    public void setTime(String time) {
        String day = time.substring(5,7);
        String month = time.substring(8,11);
        String year = time.substring(12,16);
        String hour = time.substring(17,19);
        String minute = time.substring(20,22);
        String second = time.substring(23,25);

        int D = Integer.parseInt(day);
        int M = parseMonth(month);
        int Y = Integer.parseInt(year);
        int h = Integer.parseInt(hour);
        int m = Integer.parseInt(minute);
        int s = Integer.parseInt(second);

        Time exp = new Time();
        exp.set(s,m,h,M,D,Y);
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

    public String toString() {
        return key + "=" + token + ";";
    }


}
