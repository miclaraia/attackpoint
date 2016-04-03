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
        if (forceRefresh) {
            // Force refresh, clears cache
            mLogDatabase.removeCache(userID);
            refreshData(userID, new RefreshCallback() {
                @Override
                public void done(List<LogInfo> logList) {
                    callback.onLoaded(logList);
                }

                @Override
                public void error(VolleyError e) {
                    callback.onError(e);
                }
            });
        } else if (mLogDatabase.userIsStale(userID)) {
            // Tries to load from network, but will try
            // to load from cache if no network is available
            refreshData(userID, new RefreshCallback() {
                @Override
                public void done(List<LogInfo> logList) {
                    callback.onLoaded(logList);
                }

                @Override
                public void error(VolleyError e) {
                    callback.onError(e);
                    if (e instanceof NoConnectionError && mLogDatabase.userInCache(userID)) {
                        callback.onLoaded(mLogDatabase.getCache(userID));
                    }
                }
            });
        } else {
            // refresh not forced and user isn't stale
            callback.onLoaded(mLogDatabase.getCache(userID));
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
                callback.done(logList);
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

    private List<LogInfo> getFromCache(int userID) {
        return mLogDatabase.getCache(userID);
    }

    private void addRequest(Request request) {
        mRequestQueue.add(request);
    }
}
