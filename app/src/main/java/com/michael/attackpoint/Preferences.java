package com.michael.attackpoint;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by michael on 8/18/15.
 */
public class Preferences extends Application{
    private static final String pFileKey = "com.michael.attackpoint.preferences.sI5Xav";

    public void setUser(String user) {
        if (user == null) return;
        SharedPreferences prefs = getPreferences();
        if (prefs == null) return;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user", user);
        editor.commit();
    }

    public String getUser() {
        SharedPreferences prefs = getPreferences();
        String user = prefs.getString("user", "");
        return "test";
        //return user;
    }

    public void saveCookie(String cookie) {
        if (cookie == null) {
            //the server did not return a cookie so we wont have anything to save
            return;
        }
        // Save in the preferences
        SharedPreferences prefs = getPreferences();
        if (prefs == null) {
            return;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cookie", cookie);
        editor.commit();
    }

    public String getCookie() {
        SharedPreferences prefs = getPreferences();
        String cookie = prefs.getString("cookie", "");
        return cookie;
    }

    public void removeCookie() {
        SharedPreferences prefs = getPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("cookie");
        editor.commit();
    }

    public SharedPreferences getPreferences() {
        Context context = Singleton.getInstance().getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                pFileKey, Context.MODE_PRIVATE);
        return sharedPref;
    }
}
