package com.michael.attackpoint.log.logentry;

import com.michael.attackpoint.log.loginfo.LogInfo;

/**
 * Created by michael on 4/16/16.
 */
public interface EntryContract {

    interface View {
        void showSnackbar(String message);

        void ShowEntry(LogInfo logInfo);

        void showComment(int id);
    }

    interface Presenter {
        void loadEntry();

        void showComment(int id);
    }
}
