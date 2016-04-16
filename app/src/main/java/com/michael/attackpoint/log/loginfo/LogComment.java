package com.michael.attackpoint.log.loginfo;

import com.michael.attackpoint.log.loginfo.LogComment.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by michael on 2/11/16.
 */
public class LogComment extends LogInfoItem<List<Comment>> {
    private static final String JSON = "comment";
    private static final String JSON_TITLE = "comment_title";
    private static final String JSON_AUTHOR = "comment_author";
    private static final String JSON_ID = "comment_id";

    public LogComment() {
        super();
    }

    public LogComment(JSONObject json) {
        super(json);
    }

    public LogComment(List<Comment> comment) {
        super();
        set(comment);
    }

    @Override
    public void onCreate() {
        mItem = new ArrayList<>();
    }

    @Override
    public boolean isEmpty() {
        if (mItem.size() < 1) return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Comment comment : mItem) {
            builder.append(comment.toString());
            builder.append("\n");
        }

        return builder.toString();
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Comment comment : mItem) {
                JSONObject object = new JSONObject();
                object.put(JSON_TITLE, comment.getTitle());
                object.put(JSON_AUTHOR, comment.getAuthor());
                object.put(JSON_ID, comment.getID());
                jsonArray.put(object);
            }

            json.put(JSON, jsonArray);
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fromJSON(JSONObject json) {
        try {
            mItem = new ArrayList<>();

            JSONArray jsonArray = json.getJSONArray(JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String title = object.getString(JSON_TITLE);
                String author = object.getString(JSON_AUTHOR);
                int id = object.getInt(JSON_ID);

                Comment comment = new Comment(title, author, id);
                mItem.add(comment);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Comment {
        private String title;
        private String author;
        private int id;

        public Comment(String title, String author, int id) {
            this.title = title;
            this.author = author;
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public int getID() {
            return id;
        }

        public String toString() {
            return String.format(Locale.US, "%s %d", title, id);
        }
    }
}
