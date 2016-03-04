package com.michael.attackpoint.log.loginfo;

import com.michael.attackpoint.training.ActivityTable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 2/28/16.
 */
public class LogInfoActivity extends LogInfoItem<String> {
    private static String JSON = "activity";
    private ActivityTable mTable = new ActivityTable();

    public LogInfoActivity() {
        super();
        mTable = new ActivityTable();
        mItem = mTable.getFirst();
    }

    public LogInfoActivity(JSONObject json) {
        super(json);
        mTable = new ActivityTable();
    }

    public LogInfoActivity(String name) {
        super();
        mTable = new ActivityTable();
        set(name);

    }

    @Override
    public void onCreate() {
        mItem = "";
    }

    @Override
    public boolean isEmpty() {
        if (mItem.equals("")) return true;
        return false;
    }

    @Override
    public String toString() {
        return mItem;
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

    public Integer getID() {
        Integer id = mTable.getValue(mItem);
        return id;
    }
}
