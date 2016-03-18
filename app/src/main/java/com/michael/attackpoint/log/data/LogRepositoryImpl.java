package com.michael.attackpoint.log.data;

import android.support.annotation.NonNull;
import android.util.ArrayMap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.util.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 3/17/16.
 */
public class LogRepositoryImpl implements LogRepository {
    private LogDatabase mLogDatabase;
    private RequestQueue mRequestQueue;

    public LogRepositoryImpl() {
        mLogDatabase = new LogDatabase();
        mRequestQueue = Volley.newRequestQueue(Singleton.getInstance().getContext());
    }

    @Override
    public void getLog(@NonNull final int userID, final @NonNull LoadLogCallback callback) {
        if (mLogDatabase.mLogCacheUpdate.userIsStale(userID)) {
            refreshData(userID, new RefreshCallback() {
                @Override
                public void done() {
                    getLog(userID, callback);
                }
            });
        } else {
            ArrayMap<Integer, String> cache = mLogDatabase.mLogCache.getCachedLog(userID);

            List<LogInfo> processedCache = new ArrayList<>();
            for (String entry : cache.values()) {
                LogInfo logInfo = LogInfo.getFromJSON(entry);
                processedCache.add(logInfo);
            }

            callback.onLoaded(processedCache);
        }
    }

    @Override
    public void getLogEntry(@NonNull int userID, @NonNull int id,
                            @NonNull LoadLogEntryCallback callback) {
        if (mLogDatabase.mlc)
        String entry = mLogDatabase.mLogCache.getCachedEntry(userID, id);

        LogInfo logInfo = LogInfo.getFromJSON(entry);
        logInfo.setID(id);

        callback.onLoaded(logInfo);
    }

    @Override
    public void saveLogEntry(@NonNull int userID, @NonNull LogInfo logInfo) {
        mLogDatabase.mLogCache.addCacheEntry(userID,
                logInfo.getID(), logInfo.toJSON().toString());
    }

    @Override
    public void refreshData(@NonNull final int userID, final @NonNull RefreshCallback callback) {
        getLogFromNetwork(userID, new LogCacheApi.LogCacheCallback<List<LogInfo>>() {
            @Override
            public void onLoaded(List<LogInfo> entries) {
                replaceCache(userID, entries);
                callback.done();
            }
        });
    }

    private void getLogFromNetwork(final int userID,
                                   final LogCacheApi.LogCacheCallback<List<LogInfo>> callback) {
        Request request = new LogRequest(userID, new Response.Listener<List<LogInfo>>() {
            @Override
            public void onResponse(List<LogInfo> logList) {
                replaceCache(userID, logList);
                callback.onLoaded(logList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });

        addRequest(request);
    }

    private void replaceCache(int userID, List<LogInfo> entries) {
        ArrayMap<Integer, String> cache = new ArrayMap<>();
        for (LogInfo entry : entries) {
            String json = entry.toJSON().toString();
            cache.put(entry.getID(), json);
        }

        mLogDatabase.mLogCache.removeCache(userID);
        mLogDatabase.mLogCache.putCache(userID, cache);
        mLogDatabase.mLogCacheUpdate.updateUser(userID);
    }

    private void addRequest(Request request) {
        mRequestQueue.add(request);
    }
}
