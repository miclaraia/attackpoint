package com.michael.attackpoint.log.data;

import android.util.ArrayMap;

import com.michael.attackpoint.log.loginfo.LogInfo;

import java.util.List;

/**
 * Created by michael on 3/16/16.
 */
public interface LogCacheApi {

    interface LogCacheCallback<T> {
        void onLoaded(T entries);
    }

    interface Database {
        List<LogInfo> getCache(int userID);

        LogInfo getCacheEntry(int userID, int id);

        void addCache(int userID, List<LogInfo> cache);

        void addCacheEntry(int userID, LogInfo entry);

        void removeCache(int userID);

        boolean userInCache(int userID);

        boolean userIsStale(int userID);

    }

    void getCachedLog(int userID, LogCacheCallback<List<LogInfo>> callback);

    void getCachedEntry(int userID, int id, LogCacheCallback<LogInfo> callback);

    void addEntry(int userID, LogInfo entry);
}
