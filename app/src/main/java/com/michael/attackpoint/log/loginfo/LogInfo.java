package com.michael.attackpoint.log.loginfo;

import android.text.Html;
import android.text.format.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public static final String JSON_TYPE = "type";
    public static final String JSON_TEXT = "text";
    public static final String JSON_DATE = "date";
    public static final String JSON_DISTANCE = "distance";
    public static final String JSON_UNIT = "unit";
    public static final String JSON_TIME = "time";
    public static final String JSON_PACE = "pace";
    public static final String JSON_INTENSITY = "intensity";
    public static final String JSON_COLOR = "color";
    public static final String JSON_COMMENTS = "comment";
    public static final String JSON_CLIMB = "climb";

    public String type;
    public String text;
    public String snippet;
    public int intensity;
    // TODO
    public List<Comment> comments;
    public String session;

    public Date date;
    public Duration time;
    public Pace pace;
    public Distance distance;
    public Color color;
    public Climb climb;

    public LogInfo() {
        init();
    }

    /**
     * Recreates LogInfo object from data serialized into JSON
     * @param jsonString JSON string output from tostring()
     */
    public LogInfo(String jsonString) {
        init();
        fromJSON(jsonString);
    }

    public void init() {
        date = new Date();
        time = new Duration();
        pace = new Pace();
        distance = new Distance();
        color = new Color();
        comments = new ArrayList<>();
        climb = new Climb();
    }

    public void fromJSON(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            setType((String) json.get(JSON_TYPE));
            setText((String) json.get(JSON_TEXT));

            date.fromJSON(json.getString(JSON_DATE));

            setTime((String) json.get(JSON_TIME));

            //makes sure distance actually exists before trying to set it
            if(!json.isNull(JSON_DISTANCE)) {
                setDistance((String) json.get(JSON_DISTANCE));
                //only sets pace if distance is non zero
                if (!this.distance.isEmpty()) setPace(time, distance);
            }

            this.climb.fromJSON((JSONObject) json.get(JSON_CLIMB));

            this.setIntensity((int) json.get(JSON_INTENSITY));
            this.setColor((int) json.get(JSON_COLOR));

            // get comments from JSON
            JSONArray comments_array= json.getJSONArray(JSON_COMMENTS);
            for (int i = 0; i < comments_array.length(); i++) {
                JSONObject c = comments_array.getJSONObject(i);
                addComment(new Comment(c));
            }

        } catch (JSONException e) {
            e.printStackTrace();
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

    /**
     * Sets description text of log entry
     * @param text
     */
    public void setText(String text) {
        // TODO preserve line breaks
        text = Html.fromHtml(text).toString();
        this.text = text;
        setSnippet(text);
    }

    /**
     * creates short snippet of description
     * @param snippet full description text
     */
    public void setSnippet(String snippet) {
        snippet = snippet.substring(0, Math.min(text.length(), SNIPPET_MAX));
        int index = snippet.lastIndexOf(' ');
        if (index > 0) {
            snippet = snippet.substring(0, index);
        }
        this.snippet = snippet.replace('\n',' ');
    }

    //++++++++++++++++++ Date +++++++++++++++++

    public Date getDate() {
        return date;
    }

    /**
     * Sets date of log entry
     * @param date see {@link Date} for proper formatting
     */
    public void setDate(String date) {
        this.date.set(date);
    }

    public void setDate(Calendar cal) {
        this.date.set(cal);
    }

    //++++++++++++++++++ Activity +++++++++++++++++

    /**
     * Sets type of workout (Activity on attackpoint.org)
     * @param type
     */
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

    /**
     * Sets distance
     * @param distance distance of workout
     */
    public void setDistance(Distance distance) {
        if (distance != null) {
            this.distance = distance;
        }
    }

    /**
     * Sets distance
     * @param distance distance of workout
     * @param unit unit of distance
     */
    public void setDistance(int distance, String unit) {
        this.distance = new Distance(distance, unit);
    }

    /**
     * Sets distance
     * @param distance distance of workout
     * @param unit unit of distance
     */
    public void setDistance(String distance, String unit) {
        this.distance = new Distance(distance, unit);
    }

    /**
     * Sets distance
     * @param distance distance of workout
     * @param unit unit of distance
     */
    public void setDistance(float distance, String unit) {
        this.distance = new Distance(distance, unit);
    }

    /**
     * Sets distance
     * @param distance distance of workout, string
     *                 includes unit.
     */
    public void setDistance(String distance) {
        this.distance = new Distance(distance);
    }

    /**
     * gets distance object
     * @return {@link Distance}
     */
    public Distance getDistance() {
        return distance;
    }

    //++++++++++++++++++ Time +++++++++++++++++

    /**
     * Sets time of log entry
     * @param time see {@link Time} for formatting details
     */
    public void setTime(String time) {
        this.time = new Duration(time);
    }

    /**
     * Sets time of log entry
     * @param time see {@link Time} for formatting details
     */
    public void setTime(Time time) {
        this.time = new Duration(time);
    }

    /**
     * gets time object
     * @return {@link Time}
     */
    public Duration getTime() {
        return this.time;
    }

    //++++++++++++++++++ Pace +++++++++++++++++

    public void setPace() {
        if (!this.distance.isEmpty()) {
            setPace(this.time.get(), this.distance);
        }
    }
    /**
     * calculates and sets pace of log entry
     * @param t time taken
     * @param d distance traveled
     */
    public void setPace(Duration t, Distance d) {
        this.pace = new Pace(t, d);
    }

    /**
     * calculates and sets pace of log entry
     * @param t time taken
     * @param d distance traveled
     */
    public void setPace(Time t, Distance d) {
        this.pace = new Pace(t, d);
    }

    /**
     * returns pace object
     * @return {@link Pace}
     */
    public Pace getPace() {
        return this.pace;
    }

    //++++++++++++++++++ Intensity +++++++++++++++++

    /**
     * sets intensity of log entry
     * @param intensity int from 1 to 5
     */
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    /**
     * sets intensity of log entry
     * @param intensity int from 1 to 5
     */
    public void setIntensity(String intensity) {
        if (intensity == null || intensity.equals("")) this.intensity = -1;
        else this.intensity = Integer.parseInt(intensity);
    }

    /**
     * returns intensity as string
     * @return intensity
     */
    public int getIntensity() {
        return intensity;
    }

    //++++++++++++++++++ Climb +++++++++++++++++
    public void setClimb(Climb climb) {
        this.climb = climb;
    }

    public void setClimb(String climb) {
        this.climb = new Climb(climb);
    }

    public void setClimb(int climb) {
        this.climb = new Climb(climb);
    }

    public Climb getClimb() {
        return climb;
    }

    //++++++++++++++++++ Color +++++++++++++++++

    /**
     * sets color of log entry
     * @param color
     */
    public void setColor(String color) {
        this.color = new Color(color);
    }

    /**
     * sets color of log entry
     * @param color
     */
    public void setColor(int color) {
        this.color = new Color(color);
    }

    //++++++++++++++++++ Comments +++++++++++++++++
    public void addComment(String title, String id) {
        Comment c = new Comment(title, id);
        comments.add(c);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Comment getComment(int index) {
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
    }


    /**
     * Creates a JSON string encoding each data point of LogInfo
     * to be passed through an Intent and subsequently
     * recreate this LogInfo
     * @return json string
     */
    public String toString() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON_TYPE, this.type);
            json.put(JSON_TEXT, this.text);
            json.put(JSON_DATE, this.date.toJSON());
            json.put(JSON_TIME, this.time.toString());
            json.put(JSON_DISTANCE, this.distance.toString());
            json.put(JSON_INTENSITY, this.getIntensity());
            json.put(JSON_COLOR, this.color.get());
            json.put(JSON_CLIMB, this.climb.toJSON());

            //loads comments from json
            JSONArray comments_array = new JSONArray();
            for (int i = 0; i < comments.size(); i++) {
                Comment c = comments.get(i);
                comments_array.put(c.getJSON());
            }
            json.put(JSON_COMMENTS, comments_array);

            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Container class containing public strings derived from data held in
     * a LogInfo object
     */
    public class Strings {
        public String text;
        public int color;
        public int contrast;
        public String snippet;
        public String type;
        public String date;
        public String distance;
        public String pace;
        public String time;
        public String climb;
        public String intensity;
        public String comments;
        //public String session;

        /**
         * initializes strings
         * @param li LogInfo object to be converted to strings
         */
        public Strings(LogInfo li) {
            this.text = li.text;
            this.color = li.color.get();
            this.contrast = li.color.contrast();
            this.snippet = li.snippet;
            this.type = li.type;
            this.date = li.date.toString();
            this.distance = li.distance.toString();
            this.pace = li.pace.toString();
            this.time = li.time.toString();
            this.climb = li.climb.toString();
            this.intensity = "" + li.getIntensity();
            this.comments = commentsText();

            // TODO
            //this.session = "";
        }
    }
}
