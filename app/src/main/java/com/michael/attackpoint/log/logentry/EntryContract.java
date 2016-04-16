package com.michael.attackpoint.log.logentry;

import com.michael.attackpoint.log.loginfo.LogInfo;

/**
 * Created by michael on 4/16/16.
 */
public interface EntryContract {

    interface View {
        void showSnackbar(String message);

        void ShowEntry(LogInfo logInfo);
    }

    interface Presenter {
        void loadEntry();
    }
}
