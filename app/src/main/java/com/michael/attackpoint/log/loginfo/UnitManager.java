package com.michael.attackpoint.log.loginfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 3/4/16.
 */
public class UnitManager {
    public static final String UNIT_DEFAULT = "kilometers";
    public static final Map<String, String> UNITS;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("kilometers", "km");
        map.put("miles", "mi");
        UNITS = map;
    }

    private String mUnit;

    public UnitManager() {
        mUnit = UNIT_DEFAULT;
    }

    public UnitManager(String unit) {
        mUnit = parseUnit(unit);
    }

    public String getShortUnit() {
        return UNITS.get(mUnit);
    }

    public String getLongUnit() {
        return mUnit;
    }

    private String parseUnit(String unit) {
        if (UNITS.containsKey(unit))
            return unit;
        else if (UNITS.containsValue(unit)){
            for (Map.Entry<String, String> entry : UNITS.entrySet()) {
                if (entry.getValue().equals(unit)) return entry.getKey();
            }

        }

        return UNIT_DEFAULT;
    }

    public String toString() {
        return mUnit;
    }
}
