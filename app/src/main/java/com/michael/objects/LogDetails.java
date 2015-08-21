package com.michael.objects;

import android.text.format.Time;

/**
 * Created by michael on 8/6/15.
 */


// TODO delete this class
public class LogDetails {
    public String type;
    public Time time;
    public int intensity;
    public String text;

    public Distance distance;

    public LogDetails(String type) {
        this.type = type;
    }

    //Distance
    public void setDistance(int distance, String unit) {
        this.distance = new Distance(distance, unit);
    }

    public void setDistance(String distance, String unit) {
        this.distance = new Distance(distance, unit);
    }

    public void setDistance(float distance, String unit) {
        this.distance = new Distance(distance, unit);
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    //Time
    public void setTime(String time) {
        String[] pieces = time.split(":");
        int h = 0;
        int m = 0;
        int s = 0;
        switch (pieces.length) {
            case 1:
                s = Integer.parseInt(pieces[0]);
                break;
            case 2:
                m = Integer.parseInt(pieces[0]);
                s = Integer.parseInt(pieces[1]);
                break;
            case 3:
                h = Integer.parseInt(pieces[0]);
                m = Integer.parseInt(pieces[1]);
                s = Integer.parseInt(pieces[2]);
                break;
        }
        this.time = new Time();
        this.time.set(s,m,h,0,0,0);
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = Integer.parseInt(intensity);
    }

    public void setText(String text) {
        this.text = text;
    }



    public String distanceString() {
        if (this.distance == null) {
            return null;
        }
        return this.distance.toString();
    }

    public String timeString() {
        if (this.time == null) {
            return null;
        }
        return time.format("kk:mm:ss");
    }
}
