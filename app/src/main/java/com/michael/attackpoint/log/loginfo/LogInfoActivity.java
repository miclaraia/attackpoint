package com.michael.attackpoint.log.loginfo;

import com.michael.attackpoint.training.ActivityTable;
import com.michael.attackpoint.util.AndroidFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 2/28/16.
 */
public class LogInfoActivity extends LogInfoItem<String> {
    protected static String JSON = "activity";
    private ActivityTable mTable;

    public LogInfoActivity() {
        super();
        mTable = AndroidFactory.getInstance().genActivityTable();
    }

    public LogInfoActivity(String name) {
        super();
        mTable = AndroidFactory.getInstance().genActivityTable();
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
        if (isEmpty()) return getDefault();
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

    public String getDefault() {
        return mTable.getFirst();
    }
}
