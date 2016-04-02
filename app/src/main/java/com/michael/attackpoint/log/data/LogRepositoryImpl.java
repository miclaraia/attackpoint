package com.michael.attackpoint.log.data;

import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;

import com.android.volley.NoConnectionError;
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
    private static final String DEBUG_TAG = "LogRepository";
    private LogDatabase mLogDatabase;
    private RequestQueue mRequestQueue;

    public LogRepositoryImpl() {
        mLogDatabase = new LogDatabase();
        mRequestQueue = Volley.newRequestQueue(Singleton.getInstance().getContext());
    }

    @Override
    public void getLog(@NonNull int userID, @NonNull LoadLogCallback callback) {
        getLog(false, userID, callback);
    }

    @Override
    public void getLog(boolean forceRefresh, @NonNull final int userID, @NonNull final LoadLogCallback callback) {
        if (forceRefresh || mLogDatabase.userIsStale(userID)) {
            Log.d(DEBUG_TAG, "user is stale, refreshing from network");
            refreshData(userID, new RefreshCallback() {
                @Override
                public void done() {
                    getLog(userID, callback);
                }

                @Override
                public void error(VolleyError e) {
                    callback.onNetworkError(e);
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
    public void refreshData(@NonNull final int userID,
                            final @NonNull RefreshCallback callback) {
        Request request = new LogRequest(userID, new Response.Listener<List<LogInfo>>() {
            @Override
            public void onResponse(List<LogInfo> logList) {
                replaceCache(userID, logList);
                callback.done();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                callback.error(e);
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
