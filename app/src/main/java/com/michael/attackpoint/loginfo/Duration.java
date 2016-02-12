package com.michael.attackpoint.loginfo;

/**
 * Class managing time and duration of log entries
 */
public class Duration {
    private android.text.format.Time time;

    /**
     * Creates empty duration object
     */
    public Duration() {
        this.time = new android.text.format.Time();
        this.time.set(0,0,0,0,0,0);
    }

    /**
     * Creates duration object
     * @param time time string must be a properly formatted hh:mm:ss time
     */
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

    /**
     * Creates duration object from android Time object
     * @param time
     */
    public Duration(android.text.format.Time time) {
        this.time = time;
    }

    /**
     * gets the current duration as Time object
     * @return
     */
    public android.text.format.Time get() {
        return this.time;
    }

    /**
     * returns current duration as string
     * @return
     */
    public String toString() {
        if (this.time == null) {
            return "";
        }
        String format;
        if (time.hour == 0) {
            if (time.minute == 0) {
                if (time.second == 0) return "";
                format = "%S";
            }
            else format = "%M:%S";
        }
        else format = "%H:%M:%S";
        return time.format(format);
    }

}
