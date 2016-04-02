package com.michael.attackpoint.log.data;

import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.michael.attackpoint.log.loginfo.LogInfo;

import java.util.List;

/**
 * Created by michael on 3/16/16.
 */
public interface LogRepository {

    interface LoadLogCallback {
        void onLoaded(List<LogInfo> logList);

        void onNetworkError(VolleyError e);
    }

    interface LoadLogEntryCallback {
        void onLoaded(LogInfo logInfo);
    }

    interface RefreshCallback {
        void done();

        void error(VolleyError e);
    }

    void getLog(@NonNull int userID, @NonNull LoadLogCallback callback);

    void getLog(boolean forceRefresh, @NonNull int userID, @NonNull LoadLogCallback callback);

    void getLogEntry(@NonNull int userID, @NonNull int id, @NonNull LoadLogEntryCallback callback);

    void saveLogEntry(@NonNull int userID, @NonNull LogInfo logInfo);

    void refreshData(@NonNull int userID, RefreshCallback callback);
}
