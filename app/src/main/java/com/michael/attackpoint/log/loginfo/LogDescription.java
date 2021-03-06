package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 2/28/16.
 */
public class LogDescription extends LogInfoItem<String> {
    protected static final String JSON = "description";
    protected static final int MAX_SNIPPET = 200;

    public LogDescription() {
        super();
    }

    public LogDescription(JSONObject json) {
        super(json);
    }

    public LogDescription(String description) {
        super();
        set(description);
    }

    @Override
    public void onCreate() {
        mItem = new String();
    }

    @Override
    public boolean isEmpty() {
        return mItem.isEmpty();
    }

    @Override
    public String toString() {
        return mItem.toString();
    }

    public String toSnippet() {
        if (mItem.length() < MAX_SNIPPET) return mItem;
        String snippet = mItem.substring(0, MAX_SNIPPET);
        return snippet;
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
