package com.michael.attackpoint.log.loglist;

import android.support.annotation.NonNull;

import com.michael.attackpoint.log.loginfo.LogInfo;

import java.util.List;

/**
 * Created by michael on 3/16/16.
 */
public interface LogContract {

    interface View {
        void setProgressIndicator(boolean state);

        void showLog(List<LogInfo> entries);

        void showEntryDetail(String logId);

        void showAddEntry();
    }

    interface Presenter {
        void loadLog(boolean forceUpdate);

        void addNewEntry();

        void openEntryDetails(@NonNull LogInfo requestedEntry);

    }
}
