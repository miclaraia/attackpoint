package com.michael.attackpoint.log.loginfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 3/4/16.
 */

public class Unit {
    private String name;
    private String nickname;
    private Double multiplier;

    public Unit(String name, String nickname, double multiplier) {
        this.name = name;
        this.nickname = nickname;
        this.multiplier = multiplier;
    }

    public Double convert(Double distance) {
        distance = distance * multiplier;
        return distance;
    }

    public Double standard(Double distance) {
        distance = distance / multiplier;
        return distance;
    }

    public Calendar convert(Calendar pace) {
        CalendarTime ct = new CalendarTime(pace);
        int seconds = LogPace.paceToSeconds(ct);

        int p = (int) Math.floor(seconds / multiplier);
        return LogPace.secondsToPace(p);
    }

    public Calendar standard(Calendar pace) {
        CalendarTime ct = new CalendarTime(pace);
        int seconds = LogPace.paceToSeconds(ct);

        int p = (int) Math.floor(seconds * multiplier);
        return LogPace.secondsToPace(p);
    }

    public String toString() {
        return name;
    }

    public String toNickname() {
        return nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Unit) {
            Unit u = (Unit) o;
            if (u.name.equals(this.name)) return true;
        }
        return false;
    }

    public static class UnitManager {
        private static final ArrayList<Unit> mUnits;
        static {
            ArrayList<Unit> list = new ArrayList<>();
            list.add(new Unit("kilometers", "km", 1));
            list.add(new Unit("miles", "mi", 0.625));
            list.add(new Unit("league", "lg", .2083));

            mUnits = list;
        }

        public static Unit getUnit(String unit) {
            for (Unit u : mUnits) {
                if (unit.equals(u.name) || unit.equals(u.nickname)) return u;
            }

            return getDefault();
        }

        public static Unit getDefault() {
            return getUnit("kilometers");
        }
    }
}
