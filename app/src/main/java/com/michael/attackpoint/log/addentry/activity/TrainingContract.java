package com.michael.attackpoint.log.addentry.activity;

import com.michael.attackpoint.log.addentry.pickers.ManagerContract;

/**
 * Created by michael on 4/13/16.
 */
public interface TrainingContract {
    interface View {

        void showNoNetworkError();

        void showInvalidEntryError();

        ManagerContract.Activity getManagerActivity();

    }

    interface Presenter {

        void onPause();

        void onResume();

        void onSubmit();

    }
}
