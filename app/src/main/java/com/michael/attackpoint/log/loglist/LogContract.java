package com.michael.attackpoint.log.loglist;

import android.support.annotation.NonNull;

import com.michael.attackpoint.log.loginfo.LogInfo;

import java.util.List;

/**
 * Created by michael on 3/16/16.
 */
public interface LogContract {

    interface View {
        void setProgressIndicator(boolean active);

        void showLog(List<LogInfo> entries);

        void showAddEntry();

        void showEntryDetailUi(String logId);
    }

    interface Presenter {
        void loadLog(boolean forceUpdate);

        void addNewEntry();

        void openEntryDetails(@NonNull LogInfo requestedEntry);

    }
}
