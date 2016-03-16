package com.michael.attackpoint.log.loginfo;

import org.json.JSONObject;

/**
 * Created by michael on 2/26/16.
 */
public abstract class LogInfoItem<T> {

    protected T mItem;

    public LogInfoItem() {
        onCreate();
    }

    public LogInfoItem(JSONObject json) {
        onCreate();
        fromJSON(json);
    }

    public void set(T item) {
        mItem = item;
    }

    public T get() {
        return mItem;
    }

    public String toFormString() {
        return toString();
    }

    public abstract void onCreate();
    public abstract boolean isEmpty();
    public abstract String toString();
    public abstract JSONObject toJSON(JSONObject json);
    public abstract void fromJSON(JSONObject json);
}
