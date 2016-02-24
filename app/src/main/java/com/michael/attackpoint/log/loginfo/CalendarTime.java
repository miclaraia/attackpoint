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
}
