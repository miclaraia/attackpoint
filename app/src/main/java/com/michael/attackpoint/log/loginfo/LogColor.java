package com.michael.attackpoint.log.loginfo;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class managing log entry colors
 * @author Michael Laraia
 */
public class LogColor extends LogInfoItem<Integer> {
    private static final String JSON = "color";

    public LogColor() {
        super();
    }

    public LogColor(JSONObject json) {
        super(json);
    }

    public LogColor(Integer color) {
        super();
        set(color);
    }

    @Override
    public void onCreate() {
        mItem = Color.WHITE;
    }

    @Override
    public boolean isEmpty() {
        // TODO
        return false;
    }

    @Override
    public String toString() {
        return Integer.toString(mItem);
    }

    @Override
    public JSONObject toJSON(JSONObject json) {
        try {
            json.put(JSON, mItem);
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fromJSON(JSONObject json) {
        try {
            mItem = (int) json.get(JSON);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    /*private int color = android.graphics.Color.WHITE;

    *//**
     * creates empty Color object
     *//*
    public LogColor() {

    }

    *//**
     * creates Color object
     * @param color properly formed hexadecimal color
     *//*
    public LogColor(String color) {
        this.color = android.graphics.Color.parseColor(color);
    }

    *//**
     * creates color object
     * @param color int representing hexadecimal color
     *//*
    public LogColor(int color) {
        this.color = color;
    }

    *//**
     * Gets color as integer
     * @return color
     *//*
    public int get() {
        return this.color;
    }

    *//**
     * caclulates brightness value of the current color
     * @return brightness
     *//*
    public int brightness() {
        int r = android.graphics.Color.red(color);
        int g = android.graphics.Color.green(color);
        int b = android.graphics.Color.blue(color);

        double brightness  =  Math.sqrt(.299*r*r + .587*g*g + .114*b*b);
        return (int) Math.round(brightness);
    }

    *//**
     * Returns color for text to have sufficient contrast with background color
     * @return white or black
     *//*
    public int contrast() {
        int brightness = brightness();

        if (brightness >= 186) return android.graphics.Color.BLACK;
        return android.graphics.Color.WHITE;
    }*/
}
