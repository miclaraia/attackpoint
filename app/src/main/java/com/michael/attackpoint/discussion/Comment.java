package com.michael.attackpoint.discussion;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by michael on 1/26/16.
 */
public class Comment {

    private static final String DEBUG_TAG = "ap.comment";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'kk:mm:ss'Z'";

    private String mComment;
    private int mId;
    private int mUser;
    private String mUsername;
    private Date mTimestamp;

    public Comment(String comment, int id, int user, String username, Date timestamp) {
        mComment = comment;
        mId = id;
        mUser = user;
        mUsername = username;
        mTimestamp = timestamp;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getUser() {
        return mUser;
    }

    public void setUser(int user) {
        mUser = user;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public Date getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Date timestamp) {
        mTimestamp = timestamp;
    }

    public void setTimestamp(String timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            Date parsed = sdf.parse(timestamp);
            this.mTimestamp = parsed;
        } catch (ParseException e) {
            Log.e(DEBUG_TAG, "error parsing timestamp: " + timestamp);
            e.printStackTrace();
        }
    }
}
