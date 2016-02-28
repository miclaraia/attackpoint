package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 2/28/16.
 */
public class LogInfoActivity extends LogDescription {
    private static String JSON = "activity";

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
