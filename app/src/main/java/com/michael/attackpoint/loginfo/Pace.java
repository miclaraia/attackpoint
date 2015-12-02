package com.michael.attackpoint.loginfo;

import android.text.format.*;

/**
 * Created by michael on 12/2/15.
 */
public class Pace {
    private android.text.format.Time pace;
    private String unit;
    // TODO conversion between min/km and min/mi

    public Pace(Duration t, Distance d) {
        this.unit = d.unit;
        this.pace = calc(t, d);
    }

    public Pace(android.text.format.Time time, Distance distance) {
        this.unit = distance.unit;
        set(calc(time, distance));
    }

    public void set(android.text.format.Time pace) {
        this.pace = pace;
    }

    private Time calc(Duration t, Distance d) {
        return calc(t.get(), d);
    }

    private Time calc(android.text.format.Time time, Distance distance) {
        // TODO make sure this calculates properly
        int h = time.hour;
        int m = time.minute;
        int s = time.second;

        int pace = (int) Math.floor((h*3600 + m*60 + s) / distance.distance);

        h = (int) pace / 3600;
        int r = pace % 3600;
        m = (int) r / 60;
        s = r % 60;
        android.text.format.Time out = new android.text.format.Time();
        out.set(s,m,h,0,0,0);
        return out;
    }

    public android.text.format.Time get() {
        return pace;
    }

    public String toString() {
        String out;
        if (this.pace == null) {
            return null;
        }
        if (this.pace.hour == 0) {
            out = pace.format("%M:%S");
        }
        else out = pace.format("%H:%M:%S");

        out += " / " + this.unit;
        return out;
    }
}
