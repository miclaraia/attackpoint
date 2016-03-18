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
        if (mLogDatabase.userIsStale(userID)) {
            refreshData(userID, new RefreshCallback() {
                @Override
                public void done() {
                    getLog(userID, callback);
                }
            });
        } else {
            List<LogInfo> cache = mLogDatabase.getCache(userID);
            callback.onLoaded(cache);
        }
    }

    @Override
    public void getLogEntry(@NonNull int userID, @NonNull int id,
                            @NonNull LoadLogEntryCallback callback) {
        LogInfo entry = mLogDatabase.getCacheEntry(userID, id);
        callback.onLoaded(entry);
    }

    @Override
    public void saveLogEntry(@NonNull int userID, @NonNull LogInfo logInfo) {
        mLogDatabase.addCacheEntry(userID, logInfo);
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
        mLogDatabase.removeCache(userID);
        mLogDatabase.addCache(userID, entries);
    }

    private void addRequest(Request request) {
        mRequestQueue.add(request);
    }
}
