package com.michael.attackpoint;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Class managing application preferences
 * @author Michael Laraia
 */
public class Preferences extends Application{
    private static final String DEBUG_TAG = "ap.preferences";
    private static final String pFileKey = "com.michael.attackpoint.preferences.sI5Xav";

    /**
     * sets current user
     * @param user
     */
    public void setUser(String user) {
        if (user == null) user = "";
        SharedPreferences prefs = getPreferences();
        if (prefs == null) return;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user", user);
        editor.commit();
    }

    /**
     * retrieves current user
     * @return
     */
    public String getUser() {
        SharedPreferences prefs = getPreferences();
        String user = prefs.getString("user", "");

        Log.d(DEBUG_TAG, "user in preferences: " + user);
        return user;
    }

    /**
     * initializes SharedPreferences
     * @return
     */
    public SharedPreferences getPreferences() {
        Context context = Singleton.getInstance().getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                pFileKey, Context.MODE_PRIVATE);
        return sharedPref;
    }
}
