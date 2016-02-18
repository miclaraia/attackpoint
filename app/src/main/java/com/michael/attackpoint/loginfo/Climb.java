package com.michael.attackpoint.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class containing info on distance and unit of a log entry
 * @author Michael Laraia
 */
public class Climb {
    // TODO allow conversion between units
    public int distance;
    public static final String UNIT = "m";
    public static final String JSON = "climb";


    /**
     * Creates empty distance object
     */
    public Climb() {
        distance = 0;
    }

    /**
     * Creates distance object
     * @param distance distance traveled during workout
     */
    public Climb(int distance) {
        this.distance = distance;
    }

    /**
     * Creates distance object
     * @param distance distance traveled during workout
     */
    public Climb(String distance) {
        if (!(distance == null || distance.equals(""))) {
            if (distance.contains("+")) {
                int l = distance.length();
                distance = distance.substring(1, l - 2);
            }
            this.distance = Integer.parseInt(distance);
        } else {
            this.distance = 0;
        }
    }

    public Climb(JSONObject json) {
        fromJSON(json);
    }

    /**
     * Checks if distance is 0
     * @return
     */
    public boolean isEmpty() {
        if (distance == 0) return true;
        else return false;
    }

    /**
     * Converts distance into human-readable string
     * @return
     */
    public String toString() {
        if (distance <= 0) return "";
        return Float.toString(distance) + UNIT;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON, distance);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return json;
        }
    }

    public void fromJSON(JSONObject json) {
        try {
            int d = (int) json.get(JSON);
            distance = d;
        } catch (JSONException e) {
            e.printStackTrace();
            distance = 0;
        }
    }
}