package com.michael.attackpoint.log.loginfo;

import android.text.Html;

import com.michael.attackpoint.util.AndroidFactory;

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

    private static final String JSON_ID = "_id";

    private Map<String, LogInfoItem> mItems;
    private int mID;

    public LogInfo() {
        onCreate();
    }

    /**
     * Recreates LogInfo object from data serialized into JSON
     * @param jsonString JSON string output from tostring()
     */
    public static LogInfo getFromJSON(String jsonString) {
        LogInfo logInfo = new LogInfo();
        logInfo.fromJSON(jsonString);

        return logInfo;
    }

    public void onCreate() {
        LogInfoItem climb = new LogClimb();
        LogInfoItem color = new LogColor();
        LogInfoItem comment = new LogComment();
        LogInfoItem date = new LogDate();
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

            setID(json.getInt(JSON_ID));
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

        try {
            json.put(JSON_ID, getID());
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return json;
    }

    public Strings strings() {
        return new Strings(this);
    }

    public static class Strings {
        public String activity;
        public String climb;
        public Integer color;
        public String date;
        public String description;
        public String distance;
        public String duration;
        public String intensity;
        public String pace;

        public Strings(LogInfo logInfo) {
            activity = logInfo.get(LogInfo.KEY_ACTIVITY).toString();
            climb = logInfo.get(LogInfo.KEY_CLIMB).toString();
            color = (Integer) logInfo.get(LogInfo.KEY_ACTIVITY).get();
            date = logInfo.get(LogInfo.KEY_DATE).toString();
            description = logInfo.get(LogInfo.KEY_DESCRIPTION).toString();
            distance = logInfo.get(LogInfo.KEY_DISTANCE).toString();
            duration = logInfo.get(LogInfo.KEY_DURATION).toString();
            intensity = logInfo.get(LogInfo.KEY_INTENSITY).toString();
            pace = logInfo.get(LogInfo.KEY_PACE).toString();
        }
    }
}
