package com.michael.attackpoint.loginfo;

import android.text.Html;
import android.text.format.Time;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static final String JSON_TYPE = "type";
    public static final String JSON_TEXT = "text";
    public static final String JSON_DATE = "date";
    public static final String JSON_DISTANCE = "distance";
    public static final String JSON_UNIT = "unit";
    public static final String JSON_TIME = "time";
    public static final String JSON_PACE = "pace";
    public static final String JSON_INTENSITY = "intensity";
    public static final String JSON_COLOR = "color";

    public String type;
    public String text;
    public String snippet;
    // TODO
    public String comments;
    public String session;

    public Date date;
    public Duration time;
    public Pace pace;
    public int intensity;
    public Distance distance;
    public Color color;

    /**public LogInfo(String text, String date, String type, String distance, String unit, String time) {
        init();
        this.text = text;
        this.type = type;
        this.date = new Date(date);

        this.distance.set(distance, unit);
        this.time.set(time);

        this.pace.set(this.time.get(), this.distance.get());
    }*/

    /**
     * Recreates LogInfo object from data serialized into JSON
     * @param jsonString JSON string output from tostring()
     */
    public LogInfo(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            setType((String) json.get(JSON_TYPE));
            setText((String) json.get(JSON_TEXT));
            setDate((String) json.get(JSON_DATE));

            setTime((String) json.get(JSON_TIME));
            if(!json.isNull(JSON_DISTANCE)) {
                setDistance((String) json.get(JSON_DISTANCE));
                setPace(time, distance);
            }

            this.setIntensity((int) json.get(JSON_INTENSITY));
            this.setColor((int) json.get(JSON_COLOR));

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
     * Sets type of workout (Activity on attackpoint.org)
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets date of log entry
     * @param date see {@link Date} for proper formatting
     */
    public void setDate(String date) {
        this.date.set(date);
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

    //++++++++++++++++++ Distance +++++++++++++++++
    public void setDistance(int distance, String unit) {
        this.distance = new Distance(distance, unit);
    }

    public void setDistance(String distance, String unit) {
        this.distance = new Distance(distance, unit);
    }

    public void setDistance(float distance, String unit) {
        this.distance = new Distance(distance, unit);
    }

    public void setDistance(String distance) {
        this.distance = new Distance(distance);
    }

    public Distance getDistance() {
        return distance;
    }

    //++++++++++++++++++ Time +++++++++++++++++

    public void setTime(String time) {
        this.time = new Duration(time);
    }

    public void setTime(Time time) {
        this.time = new Duration(time);
    }

    public Duration getTime() {
        return this.time;
    }

    //++++++++++++++++++ Pace +++++++++++++++++

    public void setPace(Duration t, Distance d) {
        this.pace = new Pace(t, d);
    }

    public void setPace(Time t, Distance d) {
        this.pace = new Pace(t, d);
    }

    public Pace getPace() {
        return this.pace;
    }

    //++++++++++++++++++ Intensity +++++++++++++++++

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = Integer.parseInt(intensity);
    }

    public String getIntensity() {
        return "" + intensity;
    }

    //++++++++++++++++++ Color +++++++++++++++++

    public void setColor(String color) {
        this.color = new Color(color);
    }

    public void setColor(int color) {
        this.color = new Color(color);
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
            json.put(JSON_DATE, this.date.toString());
            json.put(JSON_TIME, this.time.toString());
            json.put(JSON_DISTANCE, this.distance.toString());
            json.put(JSON_INTENSITY, this.getIntensity());
            json.put(JSON_COLOR, this.color.get());

            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

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
        public String intensity;
        public String comments;
        public String session;

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
            this.intensity = li.getIntensity();

            // TODO
            this.comments = "";
            this.session = "";
        }
    }
}
