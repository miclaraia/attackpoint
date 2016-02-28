package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 2/28/16.
 */
public class LogIntensity extends LogInfoItem<Integer> {
    private static final String JSON = "intensity";

    @Override
    public void onCreate() {
        mItem = 0;
    }

    @Override
    public boolean isEmpty() {
        if (mItem <= 0) return true;
        return false;
    }

    @Override
    public String toString() {
        return mItem.toString();
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
            mItem = (Integer) json.get(JSON);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
