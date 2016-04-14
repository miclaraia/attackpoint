package com.michael.attackpoint.log.addentry.activity;

import com.michael.attackpoint.log.addentry.pickers.ManagerContract;
import com.michael.attackpoint.log.loginfo.LogInfo;

/**
 * Created by michael on 4/13/16.
 */
public interface TrainingContract {
    interface View {

        void showNoNetworkError();

        void showInvalidEntryError();

        ManagerContract.Activity getManagerActivity();

        void createRequest(LogInfo logInfo);

    }

    interface Presenter {

        void onPause();

        void onResume();

        void onSubmit();

    }
}
