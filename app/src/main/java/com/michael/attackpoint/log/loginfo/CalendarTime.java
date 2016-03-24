package com.michael.attackpoint.log.loginfo;

import java.util.Calendar;

/**
 * Created by michael on 2/24/16.
 */
public class CalendarTime {
    public int h;
    public int m;
    public int s;

    public CalendarTime(Calendar cal) {
        h = cal.get(Calendar.HOUR_OF_DAY);
        m = cal.get(Calendar.MINUTE);
        s = cal.get(Calendar.SECOND);
    }

    public CalendarTime(int h, int m, int s) {
        this.h = h;
        this.m = m;
        this.s = s;
    }

    public static CalendarTime getEmpty() {
        return new CalendarTime(0,0,0);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CalendarTime) {
            CalendarTime ct = (CalendarTime) o;
            if (ct.h == this.h && ct.m == this.m && ct.s == this.s) {
                return true;
            }
        }
        return false;
    }
}
