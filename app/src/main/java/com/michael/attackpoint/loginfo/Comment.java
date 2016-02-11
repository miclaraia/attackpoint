package com.michael.attackpoint.loginfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 2/11/16.
 */
public class Comment {
    private String mTitle;
    private int mId;

    public static final String JSON_TITLE = "title";
    public static final String JSON_ID = "id";

    public Comment(String title, int id) {
        mTitle = title;
        mId = id;
    }

    public Comment(String title, String id) {
        mTitle = title;
        mId = Integer.parseInt(id);
    }

    public Comment(JSONObject json) {
        try {
            mTitle = (String) json.get(JSON_TITLE);
            mId = (int) json.get(JSON_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON_TITLE, mTitle);
            json.put(JSON_ID, mId);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return json;
        }
    }
}
