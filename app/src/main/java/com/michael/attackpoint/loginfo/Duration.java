package com.michael.attackpoint.loginfo;

/**
 * Created by michael on 8/6/15.
 */
public class Duration {
    private android.text.format.Time time;

    public Duration(String time) {
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
        this.time = new android.text.format.Time();
        this.time.set(s,m,h,0,0,0);
    }

    public Duration(android.text.format.Time time) {
        this.time = time;
    }

    public android.text.format.Time get() {
        return this.time;
    }

    public String toString() {
        if (this.time == null) {
            return null;
        }
        String format;
        if (time.hour == 0) {
            if (time.minute == 0) {
                format = "%S";
            }
            else format = "%M:%S";
        }
        else format = "%H:%M:%S";
        return time.format(format);
    }

}
