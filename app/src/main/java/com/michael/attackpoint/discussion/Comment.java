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
    private static final String PARSE_FORMAT = "yyyy-MM-dd'T'kk:mm:ss'Z'-zzz";
    private static final String OUT_FORMAT = "MMM dd, yyyy kk:mm:ss";

    private String mText;
    private int mId;
    private int mUser;
    private String mUsername;
    private Date mTimestamp;

    public Comment(String text, int id, int user, String username, Date timestamp) {
        setText(text);
        setId(id);
        setUser(user);
        setUsername(username);
        setTimestamp(timestamp);
    }

    public Comment(String text, int id, int user, String username, String timestamp) {
        setText(text);
        setId(id);
        setUser(user);
        setUsername(username);
        setTimestamp(timestamp);
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
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

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(OUT_FORMAT, Locale.ENGLISH);
        return sdf.format(mTimestamp);
    }

    public void setTimestamp(Date timestamp) {
        mTimestamp = timestamp;
    }

    public void setTimestamp(String timestamp) {
        timestamp += "-GMT";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(PARSE_FORMAT, Locale.ENGLISH);
            Date parsed = sdf.parse(timestamp);
            this.mTimestamp = parsed;
        } catch (ParseException e) {
            Log.e(DEBUG_TAG, "error parsing timestamp: " + timestamp);
            e.printStackTrace();
        }
    }
}
