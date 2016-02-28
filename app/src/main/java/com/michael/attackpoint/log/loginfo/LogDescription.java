package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 2/28/16.
 */
public class LogDescription extends LogInfoItem<String> {
    private static final String JSON = "description";

    @Override
    public void onCreate() {
        mItem = new String();
    }

    @Override
    public boolean isEmpty() {
        return isEmpty();
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
            mItem = (String) json.get(JSON);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
