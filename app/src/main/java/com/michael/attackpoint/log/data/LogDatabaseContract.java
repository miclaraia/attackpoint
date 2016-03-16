package com.michael.attackpoint.log.data;

import android.util.ArrayMap;

import java.util.List;

/**
 * Created by michael on 3/16/16.
 */
public interface LogDatabaseContract {

    interface Table {
        ArrayMap<Integer, String> getCachedLog(String user);

        String getCachedEntry(int id);

        void putCache(String user, List<String> cache);

        void addCacheEntry(String user, String entry);

        boolean isStale(String user);
    }
}
