package com.michael.attackpoint.log.data;

import android.support.annotation.NonNull;

import com.michael.attackpoint.log.loginfo.LogInfo;

import java.util.List;

/**
 * Created by michael on 3/16/16.
 */
public interface LogRepository {

    interface LoadLogCallback {
        void onLoaded(List<LogInfo> logList);
    }

    interface LoadLogEntryCallback {
        void onLoaded(LogInfo logInfo);
    }

    interface RefreshCallback {
        void done();
    }

    void getLog(@NonNull int userID, @NonNull LoadLogCallback callback);

    void getLogEntry(@NonNull int userID, @NonNull int id, @NonNull LoadLogEntryCallback callback);

    void saveLogEntry(@NonNull int userID, @NonNull LogInfo logInfo);

    void refreshData(@NonNull int userID, RefreshCallback callback);
}
