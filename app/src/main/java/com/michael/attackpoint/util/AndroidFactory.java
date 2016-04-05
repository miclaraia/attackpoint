package com.michael.attackpoint.util;

import android.content.ContentValues;

import com.michael.attackpoint.log.addentry.ActivityTable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 3/19/16.
 */
public class AndroidFactory {

    private static AndroidFactory mFactory;

    private AndroidFactory() {

    }

    public static synchronized AndroidFactory getInstance() {
        if (mFactory == null) {
            mFactory = new AndroidFactory();
        }

        return mFactory;
    }

    public static synchronized void setFactory(AndroidFactory factory) {
        mFactory = factory;
    }

    public ContentValues genContentValues() {
        return new ContentValues();
    }

    public JSONObject genJSONObject(String json) throws JSONException{
        return new JSONObject(json);
    }

    public JSONObject genJSONObject() {
        return new JSONObject();
    }

    public DatabaseHelper genDatabaseHelper() {
        return DatabaseHelper.getInstance(Singleton.getInstance().getContext());
    }

    public ActivityTable genActivityTable() {
        return new ActivityTable();
    }
}
