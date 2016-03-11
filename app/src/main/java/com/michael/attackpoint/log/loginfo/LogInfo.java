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
    public static final String KEY_SESSION = "ksession";
    public static final String KEY_DESCRIPTION = "kdescription";
    public static final String KEY_DISTANCE = "kdistance";
    public static final String KEY_DURATION = "kduration";
    public static final String KEY_ACTIVITY = "kactivity";
    public static final String KEY_INTENSITY = "kintensity";
    public static final String KEY_PACE = "kpace";

    private Map<String, LogInfoItem> mItems;
    private int mID;

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
        LogInfoItem climb = new LogClimb();
        LogInfoItem color = new LogColor();
        LogInfoItem comment = new LogComment();
        LogInfoItem date = new LogDate();
        LogInfoItem session = new LogSession();
        LogInfoItem description = new LogDescription();
        LogInfoItem distance = new LogDistance();
        LogInfoItem duration = new LogDuration();
        LogInfoItem activity = new LogInfoActivity();
        LogInfoItem intensity = new LogIntensity();
        LogInfoItem pace = new LogPace();

        mItems = new HashMap<>();
        mItems.put(KEY_CLIMB, climb);
        mItems.put(KEY_COLOR, color);
        mItems.put(KEY_COMMENT, comment);
        mItems.put(KEY_DATE, date);
        mItems.put(KEY_SESSION, session);
        mItems.put(KEY_DESCRIPTION, description);
        mItems.put(KEY_DISTANCE, distance);
        mItems.put(KEY_DURATION, duration);
        mItems.put(KEY_ACTIVITY, activity);
        mItems.put(KEY_INTENSITY, intensity);
        mItems.put(KEY_PACE, pace);

        mID = -1;
    }
    
    public LogInfoItem get(String key) {
        return mItems.get(key);
    }

    public void set(String key, LogInfoItem item) {
        mItems.put(key, item);
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

    public void setPace() {
        LogDistance distance = (LogDistance) mItems.get(KEY_DISTANCE);
        LogDuration duration = (LogDuration) mItems.get(KEY_DURATION);
        if (!distance.isEmpty() && !duration.isEmpty()) {
            LogPace lp = new LogPace();
            LogPace.Pace pace = LogPace.calcPace(duration, distance);
            lp.set(pace);

            mItems.remove(KEY_PACE);
            mItems.put(KEY_PACE, lp);
        }

    }

    /**
     * Subclass containing public variables as strings of
     * each data point
     * @return {@link Strings}
     */
    public Strings strings() {
        return new Strings(this);
    }

    public void setID(int id) {
        mID = id;
    }

    public int getID() {
        return mID;
    }

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
        public String session;
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
            climb = li.get(KEY_CLIMB).toString();
            color = (Integer) li.get(KEY_COLOR).get();
            comments = li.get(KEY_COMMENT).toString();
            date = li.get(KEY_DATE).toString();
            session = li.get(KEY_SESSION).toString();
            description = li.get(KEY_DESCRIPTION).toString();
            distance = li.get(KEY_DISTANCE).toString();
            duration = li.get(KEY_DURATION).toString();
            activity = li.get(KEY_ACTIVITY).toString();
            intensity = li.get(KEY_INTENSITY).toString();
            pace = li.get(KEY_PACE).toString();
        }
    }
}
