package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 2/11/16.
 */
public class LogComment extends LogInfoItem<LogComment.Comment> {
    private static final String JSON_TITLE = "comment_title";
    private static final String JSON_ID = "comment_id";

    @Override
    public void onCreate() {
        mItem = new Comment();
    }

    @Override
    public boolean isEmpty() {
        if (mItem.id < 0) return true;
        return false;
    }

    @Override
    public String toString() {
        return mItem.title;
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
        try {
            json.put(JSON_TITLE, mItem.title);
            json.put(JSON_ID, mItem.id);
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fromJSON(JSONObject json) {
        try {
            mItem.title = (String) json.get(JSON_TITLE);
            mItem.id = (int) json.get(JSON_ID);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Comment {
        public String title;
        public int id;

        public Comment() {
            title = new String();
            id = -1;
        }

        public Comment(String title, int id) {
            this.title = title;
            this.id = id;
        }
    }
    /*private String mTitle;
    private int mId;

    public static final String JSON_TITLE = "title";
    public static final String JSON_ID = "id";

    public LogComment(String title, int id) {
        mTitle = title;
        mId = id;
    }

    public LogComment(String title, String id) {
        mTitle = title;
        mId = Integer.parseInt(id);
    }

    public LogComment(JSONObject json) {
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
    }*/
}
