package com.michael.attackpoint.log.loginfo;

import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Class containing all information to be rendered on a log
 * entry's card. Has capability to serialize into JSON to
 * be passed through Intent.
 * @author Michael Laraia
 */
public class LogInfo {
    // TODO store each card temporarily in sql rather than json.
    // TODO Make next render recall faster? Can force refresh?
    private static final int SNIPPET_MAX = 50;
    public static final String NAME = "loginfo";

    public static final String KEY_CLIMB = "kclimb";
    public static final String KEY_COLOR = "kcolor";
    public static final String KEY_COMMENT = "kcomment";
    public static final String KEY_DATE = "kdate";
    public static final String KEY_DESCRIPTION = "kdescription";
    public static final String KEY_DISTANCE = "kdistance";
    public static final String KEY_DURATION = "kduration";
    public static final String KEY_ACTIVITY = "kactivity";
    public static final String KEY_INTENSITY = "kintensity";
    public static final String KEY_PACE = "kpace";

   /* public String type;
    public String text;
    public String snippet;
    public int intensity;
    // TODO
    public List<LogComment> comments;
    public String session;

    public LogDate date;
    public LogDuration duration;
    public LogPace pace;
    public LogDistance distance;
    public LogColor color;
    public LogClimb climb;*/

    private LogClimb mClimb;
    private LogColor mColor;
    private LogComment mComment;
    private LogDate mDate;
    private LogDescription mDescription;
    private LogDistance mDistance;
    private LogDuration mDuration;
    private LogInfoActivity mActivity;
    private LogIntensity mIntensity;
    private LogPace mPace;

    private Map<String, LogInfoItem> mItems;

    public LogInfo() {
        onCreate();
    }

    /**
     * Recreates LogInfo object from data serialized into JSON
     * @param jsonString JSON string output from tostring()
     */
    public LogInfo(String jsonString) {
        onCreate();
        fromJSON(jsonString);
    }

    public void onCreate() {
        mClimb = new LogClimb();
        mColor = new LogColor();
        mComment = new LogComment();
        mDate = new LogDate();
        mDescription = new LogDescription();
        mDistance = new LogDistance();
        mDuration = new LogDuration();
        mActivity = new LogInfoActivity();
        mIntensity = new LogIntensity();
        mPace = new LogPace();

        mItems = new HashMap<>();
        mItems.put(KEY_CLIMB, mClimb);
        mItems.put(KEY_COLOR, mColor);
        mItems.put(KEY_COMMENT, mComment);
        mItems.put(KEY_DATE, mDate);
        mItems.put(KEY_DESCRIPTION, mDescription);
        mItems.put(KEY_DISTANCE, mDistance);
        mItems.put(KEY_DURATION, mDuration);
        mItems.put(KEY_ACTIVITY, mActivity);
        mItems.put(KEY_INTENSITY, mIntensity);
        mItems.put(KEY_PACE, mPace);
    }

    public void fromJSON(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            for (Map.Entry<String, LogInfoItem> entry : mItems.entrySet()) {
                entry.getValue().fromJSON(json);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

            // get comments from JSON
            /*JSONArray comments_array= json.getJSONArray(JSON_COMMENTS);
            for (int i = 0; i < comments_array.length(); i++) {
                JSONObject c = comments_array.getJSONObject(i);
                addComment(new LogComment(c));
            }*/
    }

    /**
     * Subclass containing public variables as strings of
     * each data point
     * @return {@link Strings}
     */
    public Strings strings() {
        return new Strings(this);
    }

    /**
     * creates short snippet of description
     * @param snippet full description text
     */
    /*public void setSnippet(String snippet) {
        snippet = snippet.substring(0, Math.min(text.length(), SNIPPET_MAX));
        int index = snippet.lastIndexOf(' ');
        if (index > 0) {
            snippet = snippet.substring(0, index);
        }
        this.snippet = snippet.replace('\n',' ');
    }*/

    /*//++++++++++++++++++ Date +++++++++++++++++

    public LogDate getDate() {
        return date;
    }

    *//**
     * Sets date of log entry
     * @param date see {@link LogDate} for proper formatting
     *//*
    public void setDate(String date) {
        this.date.set(date);
    }

    public void setDate(Calendar cal) {
        this.date.set(cal);
    }

    public void setDate(LogDate date) {
        this.date = date;
    }

    //++++++++++++++++++ Activity +++++++++++++++++

    *//**
     * Sets type of workout (Activity on attackpoint.org)
     * @param type
     *//*
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    //++++++++++++++++++ Workout +++++++++++++++++

    public void setWorkout(String workout) {
        // TODO
    }

    public String getWorkout() {
        return "";
    }

    //++++++++++++++++++ Distance +++++++++++++++++

    *//**
     * Sets distance
     * @param distance distance of workout
     *//*
    public void setDistance(LogDistance distance) {
        if (distance != null) {
            this.distance = distance;
        }
    }

    *//**
     * Sets distance
     * @param distance distance of workout
     * @param unit unit of distance
     *//*
    public void setDistance(int distance, String unit) {
        this.distance = new LogDistance(distance, unit);
    }

    *//**
     * Sets distance
     * @param distance distance of workout
     * @param unit unit of distance
     *//*
    public void setDistance(String distance, String unit) {
        this.distance = new LogDistance(distance, unit);
    }

    *//**
     * Sets distance
     * @param distance distance of workout
     * @param unit unit of distance
     *//*
    public void setDistance(float distance, String unit) {
        this.distance = new LogDistance(distance, unit);
    }

    *//**
     * Sets distance
     * @param distance distance of workout, string
     *                 includes unit.
     *//*
    public void setDistance(String distance) {
        this.distance = new LogDistance(distance);
    }

    *//**
     * gets distance object
     * @return {@link LogDistance}
     *//*
    public LogDistance getDistance() {
        return distance;
    }

    //++++++++++++++++++ Duration +++++++++++++++++

    *//**
     * Sets duration of log entry
     * @param duration see {@link LogDuration} for formatting details
     *//*
    public void setDuration(String duration) {
        this.duration = new LogDuration(duration);
    }

    *//**
     * Sets Duration of log entry
     * @param cal see {@link LogDuration} for formatting details
     *//*
    public void setDuration(Calendar cal) {
        this.duration = new LogDuration(cal);
    }

    public void setDuration(LogDuration duration) {
        this.duration = duration;
    }

    *//**
     * gets Duration object
     * @return {@link LogDuration}
     *//*
    public LogDuration getDuration() {
        return this.duration;
    }

    //++++++++++++++++++ Pace +++++++++++++++++

    public void setPace() {
        if (!this.distance.isEmpty()) {
            setPace(this.duration.getCalendar(), this.distance);
        }
    }
    *//**
     * calculates and sets pace of log entry
     * @param t time taken
     * @param d distance traveled
     *//*
    public void setPace(LogDuration t, LogDistance d) {
        this.pace = new LogPace(t, d);
    }

    *//**
     * calculates and sets pace of log entry
     * @param t time taken
     * @param d distance traveled
     *//*
    public void setPace(Calendar t, LogDistance d) {
        this.pace = new LogPace(t, d);
    }

    *//**
     * returns pace object
     * @return {@link LogPace}
     *//*
    public LogPace getPace() {
        return this.pace;
    }

    //++++++++++++++++++ Intensity +++++++++++++++++

    *//**
     * sets intensity of log entry
     * @param intensity int from 1 to 5
     *//*
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    *//**
     * sets intensity of log entry
     * @param intensity int from 1 to 5
     *//*
    public void setIntensity(String intensity) {
        if (intensity == null || intensity.equals("")) this.intensity = -1;
        else this.intensity = Integer.parseInt(intensity);
    }

    *//**
     * returns intensity as string
     * @return intensity
     *//*
    public int getIntensity() {
        return intensity;
    }

    //++++++++++++++++++ Climb +++++++++++++++++
    public void setClimb(LogClimb climb) {
        this.climb = climb;
    }

    public void setClimb(String climb) {
        this.climb = new LogClimb(climb);
    }

    public void setClimb(int climb) {
        this.climb = new LogClimb(climb);
    }

    public LogClimb getClimb() {
        return climb;
    }

    //++++++++++++++++++ Color +++++++++++++++++

    *//**
     * sets color of log entry
     * @param color
     *//*
    public void setColor(String color) {
        this.color = new LogColor(color);
    }

    *//**
     * sets color of log entry
     * @param color
     *//*
    public void setColor(int color) {
        this.color = new LogColor(color);
    }

    //++++++++++++++++++ Comments +++++++++++++++++
    public void addComment(String title, String id) {
        LogComment c = new LogComment(title, id);
        comments.add(c);
    }

    public void addComment(LogComment comment) {
        comments.add(comment);
    }

    public List<LogComment> getComments() {
        return comments;
    }

    public LogComment getComment(int index) {
        return comments.get(index);
    }

    public int commentSize() {
        return comments.size();
    }

    public String commentsText() {
        int size = comments.size();
        String s = size + " ";
        if (size == 1) s += "comment";
        else s += "comments";
        return s;
    }*/


    /**
     * Creates a JSON string encoding each data point of LogInfo
     * to be passed through an Intent and subsequently
     * recreate this LogInfo
     * @return json string
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, LogInfoItem> entry : mItems.entrySet()) {
            json = entry.getValue().toJSON(json);
        }
        return json;
    }

    /**
     * Container class containing public strings derived from data held in
     * a LogInfo object
     */
    public class Strings {
        public String climb;
        public int color;
        public String comments;
        public String date;
        public String description;
        public String distance;
        public String duration;
        public String activity;
        public String intensity;
        public String pace;
        //public String snippet;
        //public String session;

        /**
         * initializes strings
         * @param li LogInfo object to be converted to strings
         */
        public Strings(LogInfo li) {
            climb = li.mClimb.toString();
            color = li.mColor.get();
            comments = li.mComment.toString();
            date = li.mDate.toString();
            description = li.mDescription.toString();
            distance = li.mDistance.toString();
            duration = li.mDuration.toString();
            activity = li.mActivity.toString();
            intensity = li.mIntensity.toString();
            pace = li.mPace.toString();
        }
    }
}
